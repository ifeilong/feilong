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
package com.feilong.taglib.display.barcode;

/**
 * The Class BarcodeRequestParams.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 * @deprecated since 1.10.5 推荐使用js来渲染二维码,
 *             <p>
 *             该标签会使用session机制,在页面静态化下以及高并发的场景下会有性能不高;<br>
 *             并且该标签还需要额外在web.xml中配置servlet
 *             </p>
 */
@Deprecated
class BarcodeRequestParams{

    /** <code>{@value}</code>. */
    static final String BARCODE_ID = "barcodeid";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private BarcodeRequestParams(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
}
