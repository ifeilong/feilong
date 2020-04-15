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
package com.feilong.taglib.display.breadcrumb.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.NotImplementedException;

import com.feilong.taglib.display.breadcrumb.command.BreadCrumbEntity;

/**
 * The Class BreadCrumbEntityComparator.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <PK>
 *            the generic type
 * @since 1.2.2
 * 
 * @deprecated 还没弄好
 */
@Deprecated
public class BreadCrumbEntityComparator<PK> implements Comparator<BreadCrumbEntity<PK>>,Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3270037605341961808L;

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(BreadCrumbEntity<PK> breadCrumbEntity1,BreadCrumbEntity<PK> breadCrumbEntity2){
        //TODO
        throw new NotImplementedException("compare is not implemented!");
        //        if (isNullOrEmpty(breadCrumbEntity1)){
        //            throw new NullPointerException("breadCrumbEntity1 can't be null/empty!");
        //        }
        //
        //        if (isNullOrEmpty(breadCrumbEntity2)){
        //            throw new NullPointerException("breadCrumbEntity2 can't be null/empty!");
        //        }
        //
        //        PK id1 = breadCrumbEntity1.getId();
        //        PK id2 = breadCrumbEntity2.getId();
        //
        //        PK parentId1 = breadCrumbEntity1.getParentId();
        //        PK parentId2 = breadCrumbEntity2.getParentId();
        //
        //        if (id1 == parentId2){
        //            return -1;
        //        }
        //        if (parentId1 == id2){
        //            return 1;
        //        }
        //        return 1;
        //
        //        //理论上, 没有0 的情况
    }
}
