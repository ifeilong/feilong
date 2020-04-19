package com.feilong.json.jsonlib.entity;

public class BeanIntIgnoreNullParent{

    private String                 name;

    private BeanIntIgnoreNullNests beanIntIgnoreNullNests;

    //---------------------------------------------------------------

    /**
     * 
     */
    public BeanIntIgnoreNullParent(){
        super();
    }

    /**
     * @param name
     * @param beanIntIgnoreNullNests
     */
    public BeanIntIgnoreNullParent(String name, BeanIntIgnoreNullNests beanIntIgnoreNullNests){
        super();
        this.name = name;
        this.beanIntIgnoreNullNests = beanIntIgnoreNullNests;
    }

    //---------------------------------------------------------------

    /**
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @return the beanIntIgnoreNullNests
     */
    public BeanIntIgnoreNullNests getBeanIntIgnoreNullNests(){
        return beanIntIgnoreNullNests;
    }

    /**
     * @param beanIntIgnoreNullNests
     *            the beanIntIgnoreNullNests to set
     */
    public void setBeanIntIgnoreNullNests(BeanIntIgnoreNullNests beanIntIgnoreNullNests){
        this.beanIntIgnoreNullNests = beanIntIgnoreNullNests;
    }

}