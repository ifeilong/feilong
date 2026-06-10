package com.feilong.context;

import static com.feilong.context.log.AutoLog.autoLog;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.feilong.core.Validate;
import com.feilong.core.lang.ThreadUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 轻量级重试工具,零外部依赖,适用于简单的重试场景（如调用外部 API、数据库操作等）.
 * 
 * <p>
 * 
 * 支持以下特性:
 * <ul>
 * <li>根据返回结果（如 null、空集合、空字符串）触发重试</li>
 * <li>根据异常触发重试</li>
 * <li>可配置重试次数和固定间隔</li>
 * <li>每次重试前可执行回调（如发送钉钉消息、记录日志）</li>
 * </ul>
 * 
 * </p>
 * 
 * <p>
 * 如果你的场景需要更复杂的策略（如指数退避、根据异常类型选择性重试、持久化重试状态等）, 建议使用 <span style="color:green">Spring Retry 或 Failsafe </span>等专业重试库.
 * </p>
 *
 * <h3>使用示例1 简单重试:调用 Dify 去重,空结果重试 3 次,间隔 3 秒</h3>
 * 
 * <pre>{@code
 *     Set<Long> result = SimpleRetryUtil.retry(
 *                     () -> HotBonusRemoveDeduplicationDifyApply.removeDeduplication(jsonResult),
 *                     3,
 *                     3000,
 *                     r -> r == null || r.isEmpty(),
 *                     attempt -> dingTalkBot.sendMessage("🚨 第" + attempt + "次失败,即将重试..."));
 * }
 * </pre>
 *
 * <h3>使用示例2 只对异常重试（忽略结果判断）</h3>
 * 
 * <pre>{@code
 *     String data = SimpleRetryUtil.retry(
 *                     () -> httpClient.get("https://api.example.com/data"),
 *                     3,
 *                     1000,
 *                     r -> false, // 从不因结果重试
 *                     null // 不需要加重试回调方法
 *     );
 * }
 * </pre>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.3
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleRetryUtil{

    /**
     * 轻量级重试工具，零外部依赖，适用于简单的重试场景（如调用外部 API、数据库操作等）.
     * 
     * <p>
     * 该类提供了一种简单、直接的方式来处理需要重试的操作，例如:
     * <ul>
     * <li>调用远程服务时遇到网络波动或临时错误</li>
     * <li>数据库操作遇到乐观锁冲突或死锁</li>
     * <li>调用大模型 API 时偶发超时或返回空结果</li>
     * <li>任何需要“失败后自动重试几次”的场景</li>
     * </ul>
     * </p>
     * 
     * <p>
     * 核心方法 {@link #retry(Callable, int, Long, Predicate, Consumer)} 支持以下特性:
     * <ul>
     * <li><b>结果判定重试</b>:通过 {@link Predicate} 判断返回值是否符合重试条件（如 null、空集合、特定错误码）</li>
     * <li><b>异常重试</b>:任何 {@link Throwable} 都会触发重试（包括 {@link Error}）</li>
     * <li><b>可配置重试次数</b>:指定最大尝试次数（包含首次）</li>
     * <li><b>可配置重试间隔</b>:固定等待时间，支持 {@code null} 表示不等待立即重试</li>
     * <li><b>重试回调</b>:每次重试前执行自定义操作（如发送钉钉消息、记录日志、更新监控指标）</li>
     * </ul>
     * </p>
     * 
     * <p>
     * <b>设计原则</b>:简单、透明、零依赖.如果你的场景需要更复杂的策略（如指数退避、根据异常类型选择性重试、 持久化重试状态、分布式重试协调等），建议使用 Spring Retry 或 Failsafe 等专业重试库.
     * </p>
     *
     * 
     * <h3>使用示例1:调用远程API，空结果重试3次，间隔2秒，每次重试发钉钉</h3>
     * <pre>{@code
     * String result = SimpleRetryUtil.retry(
     *     () -> remoteApi.call(request),
     *     3,
     *     2000L,
     *     r -> r == null || r.isEmpty(),
     *     attempt -> dingTalk.send("第" + attempt + "次重试")
     * );
     * }</pre>
     * 
     * <h3>使用示例2:数据库操作，只重试异常，不等待</h3>
     * <pre>{@code
     * User user = SimpleRetryUtil.retry(
     *     () -> userDao.findById(id),
     *     3,
     *     null,
     *     null,
     *     null
     * );
     * }</pre>
     * 
     * <h3>使用示例3:文件上传，结果包含错误码时重试，最多5次，间隔1秒</h3>
     * <pre>{@code
     * UploadResult uploadResult = SimpleRetryUtil.retry(
     *     () -> fileService.upload(file),
     *     5,
     *     1000L,
     *     r -> r.getCode() == 500,
     *     attempt -> log.warn("上传失败，第{}次重试", attempt)
     * );
     * }</pre>
     * 
     * @param task
     *            要执行的任务（{@link Callable}）,不能为 {@code null}
     * @param maxAttempts
     *            最大尝试次数（包含首次）,必须大于 0
     * @param intervalMillis
     *            重试间隔（毫秒）,必须大于等于 0 ; 如果为 null 表示不等待,直接重试
     * @param resultRetryPredicate
     *            判断结果是否需要重试的条件（例如 {@code result == null || result.isEmpty()}）,
     *            如果返回 {@code true} 则触发重试. 如果传入null , 则不对结果进行重试判定
     * @param onRetryAction
     *            每次重试前的回调（参数为当前已失败的次数,从 1 开始）, 可为 {@code null}（不执行任何操作）
     * @param <T>
     *            返回值类型
     * @return 最终结果:
     *         <ul>
     *         <li>如果某次执行成功且结果不符合重试条件，返回该结果</li>
     *         <li>如果所有重试耗尽且最后一次是异常，抛出该异常</li>
     *         <li>如果所有重试耗尽且最后一次结果仍符合重试条件，返回该结果（可能为 {@code null}）</li>
     *         </ul>
     * @throws Throwable
     *             如果所有重试耗尽且最后一次执行抛出异常，则抛出该异常
     * @throws IllegalArgumentException
     *             如果 {@code maxAttempts <= 0} 或者(intervalMillis传入的小于0)
     * @throws NullPointerException
     *             如果 {@code task} 为 {@code null}
     */
    public static <T> T retry(
                    Callable<T> task,
                    int maxAttempts, //最大尝试次数（包含首次）,必须大于 0
                    Long intervalMillis, //重试间隔（毫秒）,必须大于等于 0
                    Predicate<T> resultRetryPredicate,
                    Consumer<Integer> onRetryAction) throws Throwable{
        Validate.notNull(task, "task can't be null!");
        if (maxAttempts <= 0){
            throw new IllegalArgumentException(formatPattern("maxAttempts:[{}] must be > 0", maxAttempts));
        }
        if (null != intervalMillis && intervalMillis < 0){
            throw new IllegalArgumentException(formatPattern("intervalMillis:{} 要么为null,要么必须大于0", intervalMillis));
        }
        //---------------------------------------------------------------
        String taskName = task.getClass().getCanonicalName();
        Throwable lastException = null;
        for (int currentAttempt = 1; currentAttempt <= maxAttempts; currentAttempt++){
            try{
                T result = task.call();
                if (null == resultRetryPredicate){
                    return result; // 如果没有传结果重试判定器, 那么直接返回
                }
                //如果结果 不符合重试条件判定, 那么也返回
                if (!resultRetryPredicate.test(result)){
                    return result; // 成功,直接返回
                }
                //---------------------------------------------------------------
                log.warn(
                                autoLog(
                                                "第{}次任务异常 task:[{}] 结果命中resultRetryPredicate:[{}] 将会走重试判定",
                                                currentAttempt,
                                                taskName,
                                                resultRetryPredicate.getClass().getCanonicalName()));
                lastException = null; // 结果不符合条件,但不异常
                judgeRetryAttempt(currentAttempt, maxAttempts, intervalMillis, onRetryAction);
            }catch (Throwable e){ //Error也应触发重试（虽然可能性小,但更安全）
                lastException = e;
                log.warn(autoLog(formatPattern("第{}次任务异常 task:[{}]", currentAttempt, taskName), e));
                judgeRetryAttempt(currentAttempt, maxAttempts, intervalMillis, onRetryAction);
            }
        }

        //---------------------------------------------------------------
        // 所有重试耗尽
        if (lastException != null){
            throw lastException;
        }
        return null; // 没有异常但结果一直不符合条件,返回 null
    }

    //---------------------------------------------------------------
    /**
     * 判断当前尝试是否需要继续重试，并在需要时执行回调与等待.
     * 
     * <p>
     * 判定逻辑:
     * <ul>
     * <li>如果 {@code currentAttempt < maxAttempts}，说明还有重试机会，则:
     * <ul>
     * <li>如果 {@code onRetryAction} 不为 null，调用它（参数为当前尝试次数）</li>
     * <li>如果 {@code intervalMillis} 不为 null，等待指定毫秒数</li>
     * </ul>
     * </li>
     * <li>否则（已达到最大重试次数），记录警告日志</li>
     * </ul>
     * </p>
     *
     * @param currentAttempt
     *            当前尝试次数（从1开始）
     * @param maxAttempts
     *            最大尝试次数
     * @param intervalMillis
     *            重试间隔（毫秒），为 null 表示不等待
     * @param onRetryAction
     *            重试回调，为 null 表示不执行回调
     */
    private static void judgeRetryAttempt(int currentAttempt,int maxAttempts,Long intervalMillis,Consumer<Integer> onRetryAction){
        // 如果不是最后一次,执行回调并等待
        if (currentAttempt < maxAttempts){
            if (onRetryAction != null){
                onRetryAction.accept(currentAttempt);
            }else{
                log.info(autoLog("currentAttempt:[{}] 未设置重试回调", currentAttempt));
            }
            if (null != intervalMillis){
                ThreadUtil.sleep(intervalMillis);
            }
        }else{
            log.warn(autoLog("已达到最大重试次数:[{}] 不再继续重试", maxAttempts));
        }
    }
}