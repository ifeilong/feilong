package com.feilong.excel.consultant;

import java.util.Date;

/**
 * 咨询Command
 */
public class ConsultantCommand{

    /** 咨询序号 **/
    private Long    id;

    /** 商品编号 **/
    private Long    itemId;

    /** 商品名称 */
    private String  itemName;

    /** 商品编号 */
    private String  itemCode;

    /** 咨询类型编号 **/
    private Integer consultype;

    /** 咨询内容 **/
    private String  content;

    /** 创建会员编号 **/
    private Long    memberId;

    /** 创建咨询的会员名字 **/
    private String  memberName;

    /** 创建时间 **/
    private Date    createTime;

    /** 状态编号 **/
    private Integer lifeCycle;

    /** **/
    private Integer publishMark;

    /** 延时人员 **/
    private Long    responseId;

    /** 延时时间 **/
    private Date    responseTime;

    /** 回复内容 **/
    private String  resolveNote;

    /** 回复人员编号 **/
    private Long    resolveId;

    /** 回复人员的名字 **/
    private String  resolveName;

    /** 回复时间 **/
    private Date    resolveTime;

    /** 更新人员编号 **/
    private Long    lastUpdateId;

    /** 更新人员名字 **/
    private String  lastUpdateName;

    /** 更新时间 **/
    private Date    lastUpdateTime;

    /** 公示人员编号 **/
    private Long    publishId;

    /** 公示时间 **/
    private Date    publishTime;

    /** 取消公示人员编号 **/
    private Long    unPublishId;

    /** 取消公示时间 **/
    private Date    unPublishTime;

    /** 图片路径 **/
    private String  picUrl;

    /**
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * @return the itemId
     */
    public Long getItemId(){
        return itemId;
    }

    /**
     * @param itemId
     *            the itemId to set
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * @return the consultype
     */
    public Integer getConsultype(){
        return consultype;
    }

    /**
     * @param consultype
     *            the consultype to set
     */
    public void setConsultype(Integer consultype){
        this.consultype = consultype;
    }

    /**
     * @return the content
     */
    public String getContent(){
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content){
        this.content = content;
    }

    /**
     * @return the memberId
     */
    public Long getMemberId(){
        return memberId;
    }

    /**
     * @param memberId
     *            the memberId to set
     */
    public void setMemberId(Long memberId){
        this.memberId = memberId;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * @return the lifeCycle
     */
    public Integer getLifeCycle(){
        return lifeCycle;
    }

    /**
     * @param lifeCycle
     *            the lifeCycle to set
     */
    public void setLifeCycle(Integer lifeCycle){
        this.lifeCycle = lifeCycle;
    }

    /**
     * @return the publishMark
     */
    public Integer getPublishMark(){
        return publishMark;
    }

    /**
     * @param publishMark
     *            the publishMark to set
     */
    public void setPublishMark(Integer publishMark){
        this.publishMark = publishMark;
    }

    /**
     * @return the responseId
     */
    public Long getResponseId(){
        return responseId;
    }

    /**
     * @param responseId
     *            the responseId to set
     */
    public void setResponseId(Long responseId){
        this.responseId = responseId;
    }

    /**
     * @return the responseTime
     */
    public Date getResponseTime(){
        return responseTime;
    }

    /**
     * @param responseTime
     *            the responseTime to set
     */
    public void setResponseTime(Date responseTime){
        this.responseTime = responseTime;
    }

    /**
     * @return the resolveNote
     */
    public String getResolveNote(){
        return resolveNote;
    }

    /**
     * @param resolveNote
     *            the resolveNote to set
     */
    public void setResolveNote(String resolveNote){
        this.resolveNote = resolveNote;
    }

    /**
     * @return the resolveId
     */
    public Long getResolveId(){
        return resolveId;
    }

    /**
     * @param resolveId
     *            the resolveId to set
     */
    public void setResolveId(Long resolveId){
        this.resolveId = resolveId;
    }

    /**
     * @return the resolveTime
     */
    public Date getResolveTime(){
        return resolveTime;
    }

    /**
     * @param resolveTime
     *            the resolveTime to set
     */
    public void setResolveTime(Date resolveTime){
        this.resolveTime = resolveTime;
    }

    /**
     * @return the lastUpdateId
     */
    public Long getLastUpdateId(){
        return lastUpdateId;
    }

    /**
     * @param lastUpdateId
     *            the lastUpdateId to set
     */
    public void setLastUpdateId(Long lastUpdateId){
        this.lastUpdateId = lastUpdateId;
    }

    /**
     * @return the lastUpdateTime
     */
    public Date getLastUpdateTime(){
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     *            the lastUpdateTime to set
     */
    public void setLastUpdateTime(Date lastUpdateTime){
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the publishId
     */
    public Long getPublishId(){
        return publishId;
    }

    /**
     * @param publishId
     *            the publishId to set
     */
    public void setPublishId(Long publishId){
        this.publishId = publishId;
    }

    /**
     * @return the publishTime
     */
    public Date getPublishTime(){
        return publishTime;
    }

    /**
     * @param publishTime
     *            the publishTime to set
     */
    public void setPublishTime(Date publishTime){
        this.publishTime = publishTime;
    }

    /**
     * @return the unPublishId
     */
    public Long getUnPublishId(){
        return unPublishId;
    }

    /**
     * @param unPublishId
     *            the unPublishId to set
     */
    public void setUnPublishId(Long unPublishId){
        this.unPublishId = unPublishId;
    }

    /**
     * @return the unPublishTime
     */
    public Date getUnPublishTime(){
        return unPublishTime;
    }

    /**
     * @param unPublishTime
     *            the unPublishTime to set
     */
    public void setUnPublishTime(Date unPublishTime){
        this.unPublishTime = unPublishTime;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public String getItemName(){
        return itemName;
    }

    public void setMemberName(String memberName){
        this.memberName = memberName;
    }

    public String getMemberName(){
        return memberName;
    }

    public String getResolveName(){
        return resolveName;
    }

    public void setResolveName(String resolveName){
        this.resolveName = resolveName;
    }

    public void setLastUpdateName(String lastUpdateName){
        this.lastUpdateName = lastUpdateName;
    }

    public String getLastUpdateName(){
        return lastUpdateName;
    }

    public String getItemCode(){
        return itemCode;
    }

    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }

    public String getPicUrl(){
        return picUrl;
    }

    public void setPicUrl(String picUrl){
        this.picUrl = picUrl;
    }

}
