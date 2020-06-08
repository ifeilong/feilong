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

package com.feilong.lib.json.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDK 1.4+ RegexpMatcher implementation.
 * 
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 * @deprecated maybe can 删除
 */
@Deprecated
public class JdkRegexpMatcher implements RegexpMatcher{

    /** The pattern. */
    private final Pattern pattern;

    //---------------------------------------------------------------

    /**
     * Instantiates a new jdk regexp matcher.
     *
     * @param pattern
     *            the pattern
     */
    public JdkRegexpMatcher(String pattern){
        this(pattern, false);
    }

    /**
     * Instantiates a new jdk regexp matcher.
     *
     * @param pattern
     *            the pattern
     * @param multiline
     *            the multiline
     */
    public JdkRegexpMatcher(String pattern, boolean multiline){
        if (multiline){
            this.pattern = Pattern.compile(pattern, Pattern.MULTILINE);
        }else{
            this.pattern = Pattern.compile(pattern);
        }
    }

    /**
     * Gets the group if matches.
     *
     * @param str
     *            the str
     * @param group
     *            the group
     * @return the group if matches
     */
    @Override
    public String getGroupIfMatches(String str,int group){
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()){
            return matcher.group(group);
        }
        return "";
    }

    //---------------------------------------------------------------

    /**
     * Matches.
     *
     * @param str
     *            the str
     * @return true, if successful
     */
    @Override
    public boolean matches(String str){
        return pattern.matcher(str).matches();
    }
}