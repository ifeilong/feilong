package com.feilong.excel.subscribtion;

import java.io.Serializable;

public class SubscribtionCommand implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3134310251651746158L;

    private String            email;

    private String            mobile;

    /**
     * @return the email
     */
    public String getEmail(){
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * @return the mobile
     */
    public String getMobile(){
        return mobile;
    }

    /**
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(String mobile){
        this.mobile = mobile;
    }

}
