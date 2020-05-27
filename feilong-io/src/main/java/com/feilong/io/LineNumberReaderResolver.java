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
package com.feilong.io;

/**
 * 定义对 {@link java.io.LineNumberReader} 的解析.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.4.1
 */
public interface LineNumberReaderResolver{

    /**
     * 每行读取的时候的操作.
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * try{
     *     Set{@code <String>} set = new HashSet{@code <>}();
     *     BufferedReader bufferedReader = new BufferedReader(read);
     *     String txt = null;
     *     while ((txt = bufferedReader.readLine()) != null){ <span style="color:green">// 读取文件,将文件内容放入到set中</span>
     *         txt = txt.trim();<span style="color:green">// 忽略前面前后空格</span>
     *         txt = txt.replace(" ", "");<span style="color:green">// 文中过滤空格</span>
     *         set.add(txt);
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close(); <span style="color:green">// 关闭文件流</span>
     * }
     * return set;
     * 
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * 
     * final Set{@code <String>} set = new HashSet{@code <>}();
     * 
     * IOReaderUtil.resolverFile(read, new <b>LineNumberReaderResolver</b>(){
     * 
     *     {@code @Override}
     *     public boolean resolve(int lineNumber,String line){
     *         line = line.trim();<span style="color:green">// 忽略前面前后空格</span>
     *         line = line.replace(" ", "");<span style="color:green">// 文中过滤空格</span>
     *         set.add(line);<span style="color:green">// 读取文件,将文件内容放入到set中</span>
     *         return true;
     *     }
     * });
     * return set;
     * </pre>
     * 
     * </blockquote>
     *
     * @param lineNumber
     *            行号,<br>
     *            默认从0开始,但是此处由于在循环内部调用,即上面已经开始读取了,那么此处值最小值是1<br>
     *            ( see {@link java.io.LineNumberReader#read()}), see {@link java.io.LineNumberReader#getLineNumber()}
     * @param line
     *            the line {@link java.io.LineNumberReader#readLine()}
     * @return 如果返回 ture ,将会继续循环读取下一行(如果有下一行);<br>
     *         否则跳出循环;
     * @see java.io.LineNumberReader#getLineNumber()
     * @see java.io.LineNumberReader#readLine()
     * @see java.io.LineNumberReader#read()
     * @since 2.0.0 change method name from "excute" to "resolve"
     */
    boolean resolve(int lineNumber,String line);
}
