/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.context.codecreator;

/**
 * 基于序列的 code创造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public interface SequenceTypeOrderCodeCreator extends OrderCodeCreator{

    /**
     * 创建code.
     *
     * @param sequence
     *            序列唯一值,比如 pgsql的序列
     * @param maxLength
     *            最大长度
     * @return 如果 <code>sequence</code> 是 {@code <=} 0,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>maxLength</code> 是 {@code <=} 0,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>maxLength</code> 是 {@code <} <code>sequence</code> 长度,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>maxLength</code> 是 {@code >=} <code>sequence</code> 长度,直接返回 <code>sequence</code><br>
     * @since 1.10.4
     */
    String create(long sequence,int maxLength);
}
