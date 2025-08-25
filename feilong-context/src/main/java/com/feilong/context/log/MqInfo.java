package com.feilong.context.log;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * mq 消息
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2025-05-26
 */
public class MqInfo{

    /**
     * MQ类型 1rabbit 2rocket 3unknown
     */
    private String mqType;

    /**
     * MQ队列或消费者
     */
    private String mqQueueOrConsumer;

    /**
     * MQ消息内容
     */
    private String mqContent;

    //---------------------------------------------------------------

    /**
     * 消息接收应用
     */
    private String receiverApplication;

    //---------------------------------------------------------------

    /**
     * @return the mqType
     */
    public String getMqType(){
        return mqType;
    }

    /**
     * @param mqType
     *            the mqType to set
     */
    public void setMqType(String mqType){
        this.mqType = mqType;
    }

    /**
     * @return the mqQueueOrConsumer
     */
    public String getMqQueueOrConsumer(){
        return mqQueueOrConsumer;
    }

    /**
     * @param mqQueueOrConsumer
     *            the mqQueueOrConsumer to set
     */
    public void setMqQueueOrConsumer(String mqQueueOrConsumer){
        this.mqQueueOrConsumer = mqQueueOrConsumer;
    }

    /**
     * @return the mqContent
     */
    public String getMqContent(){
        return mqContent;
    }

    /**
     * @param mqContent
     *            the mqContent to set
     */
    public void setMqContent(String mqContent){
        this.mqContent = mqContent;
    }

    /**
     * @return the receiverApplication
     */
    public String getReceiverApplication(){
        return receiverApplication;
    }

    /**
     * @param receiverApplication
     *            the receiverApplication to set
     */
    public void setReceiverApplication(String receiverApplication){
        this.receiverApplication = receiverApplication;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
