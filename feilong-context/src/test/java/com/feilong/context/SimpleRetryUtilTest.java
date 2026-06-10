package com.feilong.context;

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.junit.Assert.*;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * SimpleRetryUtil 单元测试
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.3
 * 
 */
@Slf4j
public class SimpleRetryUtilTest{

    // ==================== 正常成功场景 ====================

    @Test
    public void testRetrySuccessFirstAttempt() throws Throwable{
        // 第一次调用即成功
        Callable<String> task = () -> "success";
        String result = SimpleRetryUtil.retry(task, 3, 100L, null, null);
        assertEquals("success", result);
    }

    @Test
    public void testRetrySuccessWithPredicate() throws Throwable{
        // 第一次调用返回结果符合条件（predicate返回false），直接返回
        Callable<String> task = () -> "valid";
        String result = SimpleRetryUtil.retry(task, 3, 100L, s -> s == null || s.isEmpty(), null);
        assertEquals("valid", result);
    }

    // ==================== 异常重试场景 ====================

    @Test(expected = RuntimeException.class)
    public void testRetryThrowsAfterAllAttempts() throws Throwable{
        // 始终抛出异常，重试3次后抛出最后一次异常
        Callable<String> task = () -> {
            throw new RuntimeException("persistent failure");
        };
        SimpleRetryUtil.retry(task, 3, 10L, null, null);
    }

    @Test
    public void testRetrySucceedsOnSecondAttemptAfterException() throws Throwable{
        // 第一次抛异常，第二次成功
        AtomicInteger counter = new AtomicInteger(0);
        Callable<String> task = () -> {
            if (counter.incrementAndGet() < 2){
                throw new RuntimeException("first fail");
            }
            return "success";
        };
        String result = SimpleRetryUtil.retry(task, 3, 10L, null, null);
        assertEquals("success", result);
        assertEquals(2, counter.get());
    }

    // ==================== 结果条件重试场景 ====================

    @Test
    public void testRetryOnEmptyResult() throws Throwable{
        // 前两次返回空，第三次返回非空
        AtomicInteger counter = new AtomicInteger(0);
        Callable<String> task = () -> {
            int attempt = counter.incrementAndGet();
            return attempt < 3 ? "" : "final";
        };
        String result = SimpleRetryUtil.retry(
                        task,
                        3,
                        10L,
                        s -> isNullOrEmpty(s), //
                        (currentAttempt) -> log.info("currentAttempt:{}", currentAttempt));
        assertEquals("final", result);
        assertEquals(3, counter.get());
    }

    @Test
    public void testRetryNeverSatisfiesCondition() throws Throwable{
        // 始终返回空，重试耗尽后返回最后一次结果（null或空）
        AtomicInteger counter = new AtomicInteger(0);
        Callable<String> task = () -> {
            counter.incrementAndGet();
            return "";
        };
        Predicate<String> isEmpty = s -> s == null || s.isEmpty();
        String result = SimpleRetryUtil.retry(task, 3, 10L, isEmpty, null);
        assertNull("重试耗尽后应返回 null", result);
        assertEquals(3, counter.get());
    }

    // ==================== 回调触发场景 ====================

    @Test
    public void testOnRetryActionInvoked() throws Throwable{
        // 前两次失败，验证回调被调用且次数正确
        AtomicInteger attempts = new AtomicInteger(0);
        List<Integer> retryAttempts = new ArrayList<>();
        Consumer<Integer> onRetry = attempt -> retryAttempts.add(attempt);

        Callable<String> task = () -> {
            int cur = attempts.incrementAndGet();
            if (cur < 3){
                throw new RuntimeException("fail " + cur);
            }
            return "ok";
        };

        SimpleRetryUtil.retry(task, 3, 10L, null, onRetry);
        assertEquals(2, retryAttempts.size()); // 第1次和第2次失败后触发回调
        assertEquals(Integer.valueOf(1), retryAttempts.get(0));
        assertEquals(Integer.valueOf(2), retryAttempts.get(1));
    }

    @Test
    public void testOnRetryActionNotInvokedOnSuccess() throws Throwable{
        // 第一次成功，不应触发回调
        List<Integer> retryAttempts = new ArrayList<>();
        Consumer<Integer> onRetry = attempt -> retryAttempts.add(attempt);

        SimpleRetryUtil.retry(() -> "ok", 3, 10L, null, onRetry);
        assertTrue(retryAttempts.isEmpty());
    }

    // ==================== null predicate 场景 ====================

    @Test
    public void testNullPredicateReturnsDirectly() throws Throwable{
        // 即使返回空字符串，但由于predicate为null，直接返回
        Callable<String> task = () -> "";
        String result = SimpleRetryUtil.retry(task, 3, 10L, null, null);
        assertEquals("", result);
    }

    // ==================== null interval 场景 ====================

    @Test
    public void testNullIntervalNoSleep() throws Throwable{
        // interval为null时不等待，快速重试
        AtomicInteger counter = new AtomicInteger(0);
        Callable<String> task = () -> {
            if (counter.incrementAndGet() < 2){
                throw new RuntimeException("fail");
            }
            return "ok";
        };
        String result = SimpleRetryUtil.retry(task, 3, null, null, null);
        assertEquals("ok", result);
    }

    // ==================== 边界条件 ====================

    @Test(expected = IllegalArgumentException.class)
    public void testZeroMaxAttempts() throws Throwable{
        SimpleRetryUtil.retry(() -> "test", 0, 100L, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxAttempts() throws Throwable{
        SimpleRetryUtil.retry(() -> "test", -1, 100L, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInterval() throws Throwable{
        SimpleRetryUtil.retry(() -> "test", 3, -1L, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTask() throws Throwable{
        SimpleRetryUtil.retry(null, 3, 100L, null, null);
    }

    // ==================== 混合场景：异常+结果条件 ====================

    @Test
    public void testMixedFailures() throws Throwable{
        // 第一次异常，第二次返回空，第三次成功
        AtomicInteger counter = new AtomicInteger(0);
        Callable<String> task = () -> {
            int cur = counter.incrementAndGet();
            if (cur == 1)
                throw new RuntimeException("exception");
            if (cur == 2)
                return "";
            return "success";
        };
        Predicate<String> isEmpty = s -> s == null || s.isEmpty();
        String result = SimpleRetryUtil.retry(task, 3, 10L, isEmpty, null);
        assertEquals("success", result);
        assertEquals(3, counter.get());
    }
}