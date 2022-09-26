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

import java.io.Serializable;

/**
 * 标识 用来做页面展示用的view.
 * <p>
 * (目前只是标识类,将来可能会有扩展).
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see SubViewCommand
 * @since 1.11.3
 * @deprecated 移动到feilong-spring since 3.3.1
 */
@Deprecated
public interface ViewCommand extends Serializable{

}
