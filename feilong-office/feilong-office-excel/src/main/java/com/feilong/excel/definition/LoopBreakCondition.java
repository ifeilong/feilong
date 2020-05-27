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
package com.feilong.excel.definition;

/**
 * 循环跳出的条件.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class LoopBreakCondition{

    /** The row offset. */
    private int    rowOffset;

    /** The col offset. */
    private int    colOffset;

    /** The flag string. */
    private String flagString;

    //---------------------------------------------------------------

    /**
     * Gets the row offset.
     *
     * @return the row offset
     */
    public int getRowOffset(){
        return rowOffset;
    }

    /**
     * Sets the row offset.
     *
     * @param rowOffset
     *            the new row offset
     */
    public void setRowOffset(int rowOffset){
        this.rowOffset = rowOffset;
    }

    //---------------------------------------------------------------

    /**
     * Gets the col offset.
     *
     * @return the col offset
     */
    public int getColOffset(){
        return colOffset;
    }

    /**
     * Sets the col offset.
     *
     * @param colOffset
     *            the new col offset
     */
    public void setColOffset(int colOffset){
        this.colOffset = colOffset;
    }

    //---------------------------------------------------------------

    /**
     * Gets the flag string.
     *
     * @return the flag string
     */
    public String getFlagString(){
        return flagString;
    }

    /**
     * Sets the flag string.
     *
     * @param flagString
     *            the new flag string
     */
    public void setFlagString(String flagString){
        this.flagString = flagString;
    }

}
