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
package com.feilong.context;

/**
 * {@link ValueLoader} 接口的简单实现, 可以快速的来构建 指定值的 {@link ValueLoader}.
 * 
 * <h3>示例:</h3>
 * 
 * 超过多少分钟的订单需要被查出来 需要设置默认值:
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * 
 * private ValueLoader{@code <Integer>} intervalMinutesValueLoader = new SimpleValueLoader{@code <>}(30);
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <V>
 *            the value type
 * @since 2.0.0
 */
public class SimpleValueLoader<V> implements ValueLoader<V>{

    /** The v. */
    private V v;

    //---------------------------------------------------------------

    /**
     * Instantiates a new simple value loader.
     */
    public SimpleValueLoader(){
        super();
    }

    /**
     * Instantiates a new simple value loader.
     *
     * @param v
     *            the v
     */
    public SimpleValueLoader(V v){
        super();
        this.v = v;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.ValueLoader#load()
     */
    @Override
    public V load(){
        return v;
    }

    //---------------------------------------------------------------

    /**
     * Sets the v.
     *
     * @param v
     *            the v to set
     */
    public void setV(V v){
        this.v = v;
    }
}
