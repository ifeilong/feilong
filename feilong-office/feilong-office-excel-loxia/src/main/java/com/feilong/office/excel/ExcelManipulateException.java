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
package com.feilong.office.excel;

import java.util.Arrays;

/**
 * The Class ExcelManipulateException.
 */
public class ExcelManipulateException extends Exception{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 478553091122313602L;

    //---------------------------------------------------------------

    /** The error code. */
    private int               errorCode;

    /** [SheetNo,Position,CurrentValue,Pattern,ChoiceList]. */
    private Object[]          args;

    //---------------------------------------------------------------

    /**
     * Instantiates a new excel manipulate exception.
     *
     * @param errorCode
     *            the error code
     * @param args
     *            the args
     */
    public ExcelManipulateException(int errorCode, Object[] args){
        this.errorCode = errorCode;
        this.args = args;
    }

    //---------------------------------------------------------------

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public int getErrorCode(){
        return errorCode;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode
     *            the new error code
     */
    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    /**
     * 获得 [SheetNo,Position,CurrentValue,Pattern,ChoiceList].
     *
     * @return the [SheetNo,Position,CurrentValue,Pattern,ChoiceList]
     */
    public Object[] getArgs(){
        return args;
    }

    /**
     * 设置 [SheetNo,Position,CurrentValue,Pattern,ChoiceList].
     *
     * @param args
     *            the new [SheetNo,Position,CurrentValue,Pattern,ChoiceList]
     */
    public void setArgs(Object[] args){
        this.args = args;
    }

    //---------------------------------------------------------------

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString(){
        return "ExcelManipulateException[" + this.errorCode + "]" + (this.args == null ? "" : Arrays.asList(this.args).toString());
    }

}
