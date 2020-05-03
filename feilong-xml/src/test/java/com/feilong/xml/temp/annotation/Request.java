package com.feilong.xml.temp.annotation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Response")
public class Request{

    @XStreamAlias("returncode")
    private String returnCode;

    @XStreamAlias("returnmessage")
    private String returnMessage;

    @XStreamAlias("return_message")
    private String return_Message;

    @XStreamAlias("status")
    private int    status;

    @XStreamAlias("time")
    private String reqTime;

    public String getReturnCode(){
        return returnCode;

    }

    public void setReturnCode(String returnCode){
        this.returnCode = returnCode;

    }

    public String getReturnMessage(){
        return returnMessage;

    }

    public void setReturnMessage(String returnMessage){
        this.returnMessage = returnMessage;

    }

    public int getStatus(){
        return status;

    }

    public void setStatus(int status){
        this.status = status;

    }

    public String getReqTime(){
        return reqTime;

    }

    public void setReqTime(String reqTime){
        this.reqTime = reqTime;

    }

    @Override

    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * @return the return_Message
     */
    public String getReturn_Message(){
        return return_Message;
    }

    /**
     * @param return_Message
     *            the return_Message to set
     */
    public void setReturn_Message(String return_Message){
        this.return_Message = return_Message;
    }

}