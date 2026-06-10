package com.feilong.io.json.dify;

import lombok.ToString;

/**
 * 当 response_mode 为 blocking 时，返回 CompletionResponse object
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
public class DifyCompletionResponse<T> {

    /**
     * (string) workflow 执行 ID
     */
    private String                        workflowRunId;

    /**
     * (string) 任务 ID，用于请求跟踪和下方的停止响应接口
     */
    private String                        taskId;

    /**
     * data (object) 详细内容
     */
    private DifyCompletionResponseData<T> data;

    //---------------------------------------------------------------

    /**
     * @return the workflowRunId
     */
    public String getWorkflowRunId(){
        return workflowRunId;
    }

    /**
     * @param workflowRunId
     *            the workflowRunId to set
     */
    public void setWorkflowRunId(String workflowRunId){
        this.workflowRunId = workflowRunId;
    }

    /**
     * @return the taskId
     */
    public String getTaskId(){
        return taskId;
    }

    /**
     * @param taskId
     *            the taskId to set
     */
    public void setTaskId(String taskId){
        this.taskId = taskId;
    }

    /**
     * @return the data
     */
    public DifyCompletionResponseData<T> getData(){
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(DifyCompletionResponseData<T> data){
        this.data = data;
    }

}
