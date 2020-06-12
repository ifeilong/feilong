package com.feilong.json.entity;

public class BeanIntIgnoreNullNests{

    /** 年龄. */
    private Integer age;

    //---------------------------------------------------------------

    /**
     * 
     */
    public BeanIntIgnoreNullNests(){
        super();
    }

    public BeanIntIgnoreNullNests(Integer age){
        super();
        this.age = age;
    }

    //---------------------------------------------------------------

    /**
     * @return the age
     */
    public Integer getAge(){
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(Integer age){
        this.age = age;
    }

}