package com.feilong.io.json.dify;

import lombok.ToString;

/**
 * The Class DifyCompletionResponseData.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 */
//public String toString(){
//  return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
//}

//Lombok: 约 50-100 ms (百万次调用)
//Commons: 约 500-1000 ms (百万次调用)
//Lombok 通常快 5-10 倍

@ToString
//@ToString(exclude = {"password", "token", "privateKey"})
public class DifyCompletionResponseData<T> {

    /** (string) workflow 执行 ID. */
    private String  id;

    /** (string) 关联 Workflow ID;. */
    private String  workflowId;

    /** (string) 执行状态, running / succeeded / failed / stopped;. */
    private String  status;

    /** (json) Optional 输出内容;. */
    private T       outputs;

    /** (string) Optional 错误原因. */
    private String  error;

    /** (float) Optional 耗时(s). */
    private String  elapsedTime;

    /** Optional 总使用 tokens. */
    private Integer totalTokens;

    /** 总步数（冗余），默认 0. */
    private Integer totalSteps;

    /** (timestamp) 开始时间. */
    private Long    createdAt;

    /** (timestamp) 结束时间. */
    private Long    finishedAt;

    //---------------------------------------------------------------

    /**
     * @return the id
     */
    public String getId(){
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * @return the workflowId
     */
    public String getWorkflowId(){
        return workflowId;
    }

    /**
     * @param workflowId
     *            the workflowId to set
     */
    public void setWorkflowId(String workflowId){
        this.workflowId = workflowId;
    }

    /**
     * @return the status
     */
    public String getStatus(){
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status){
        this.status = status;
    }

    /**
     * @return the error
     */
    public String getError(){
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(String error){
        this.error = error;
    }

    /**
     * @return the elapsedTime
     */
    public String getElapsedTime(){
        return elapsedTime;
    }

    /**
     * @param elapsedTime
     *            the elapsedTime to set
     */
    public void setElapsedTime(String elapsedTime){
        this.elapsedTime = elapsedTime;
    }

    /**
     * @return the totalTokens
     */
    public Integer getTotalTokens(){
        return totalTokens;
    }

    /**
     * @param totalTokens
     *            the totalTokens to set
     */
    public void setTotalTokens(Integer totalTokens){
        this.totalTokens = totalTokens;
    }

    /**
     * @return the totalSteps
     */
    public Integer getTotalSteps(){
        return totalSteps;
    }

    /**
     * @param totalSteps
     *            the totalSteps to set
     */
    public void setTotalSteps(Integer totalSteps){
        this.totalSteps = totalSteps;
    }

    /**
     * @return the createdAt
     */
    public Long getCreatedAt(){
        return createdAt;
    }

    /**
     * @param createdAt
     *            the createdAt to set
     */
    public void setCreatedAt(Long createdAt){
        this.createdAt = createdAt;
    }

    /**
     * @return the finishedAt
     */
    public Long getFinishedAt(){
        return finishedAt;
    }

    /**
     * @param finishedAt
     *            the finishedAt to set
     */
    public void setFinishedAt(Long finishedAt){
        this.finishedAt = finishedAt;
    }

    /**
     * @return the outputs
     */
    public T getOutputs(){
        return outputs;
    }

    /**
     * @param outputs
     *            the outputs to set
     */
    public void setOutputs(T outputs){
        this.outputs = outputs;
    }
}
