package com.feilong.json.jsonlib.entity;

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

    /**
     * @param age
     * @param name
     */
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