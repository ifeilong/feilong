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

package com.feilong.json.lib.json.regexp;

/**
 * Convenience utility for working withRegexpMatcher.<br>
 * 
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class RegexpUtils{

    /**
     * Returns a RegexpMatcher that works in a specific environment.<br>
     * When in a JVM 1.3.1 it will return a Perl5RegexpMatcher, if the JVM is
     * younger (1.4+) it will return a JdkRegexpMatcher.
     *
     * @param pattern
     *            the pattern
     * @return the matcher
     */
    public static RegexpMatcher getMatcher(String pattern){
        return new JdkRegexpMatcher(pattern);
    }

    /**
     * Returns a RegexpMatcher that works in a specific environment.<br>
     * When in a JVM 1.3.1 it will return a Perl5RegexpMatcher, if the JVM is
     * younger (1.4+) it will return a JdkRegexpMatcher.
     *
     * @param pattern
     *            the pattern
     * @param multiline
     *            the multiline
     * @return the matcher
     */
    public static RegexpMatcher getMatcher(String pattern,boolean multiline){
        return new JdkRegexpMatcher(pattern, true);
    }

    /**
     * Instantiates a new regexp utils.
     */
    private RegexpUtils(){

    }
}