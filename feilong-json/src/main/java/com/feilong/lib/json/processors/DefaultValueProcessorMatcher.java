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

package com.feilong.lib.json.processors;

import java.util.Set;

/**
 * Base class for finding a matching DefaultValueProcessor.<br>
 * <ul>
 * <li>DEFAULT - matches the target class with equals().</li>
 * </ul>
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public abstract class DefaultValueProcessorMatcher{

    /** Matches the target with equals(). */
    public static final DefaultValueProcessorMatcher DEFAULT = new DefaultDefaultValueProcessorMatcher();

    /**
     * Returns the matching class calculated with the target class and the
     * provided set.
     *
     * @param target
     *            the target class to match
     * @param set
     *            a set of possible matches
     * @return the match
     */
    public abstract Object getMatch(Class target,Set set);

    /**
     * The Class DefaultDefaultValueProcessorMatcher.
     */
    private static final class DefaultDefaultValueProcessorMatcher extends DefaultValueProcessorMatcher{

        /**
         * Gets the match.
         *
         * @param target
         *            the target
         * @param set
         *            the set
         * @return the match
         */
        @Override
        public Object getMatch(Class target,Set set){
            if (target != null && set != null && set.contains(target)){
                return target;
            }
            return null;
        }
    }
}