package com.feilong.json.jsonlib.entity;

public class BeanIntIgnoreNull{

    /** 年龄. */
    private Integer age;

    private String  name;

    //---------------------------------------------------------------

    /**
     * 
     */
    public BeanIntIgnoreNull(){
        super();
    }

    /**
     * @param age
     * @param name
     */
    public BeanIntIgnoreNull(Integer age, String name){
        super();
        this.age = age;
        this.name = name;
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

}