package com.feilong.json.entity;

import java.util.Date;

public class BeanWithDate{

    private String name;

    private Date   date;

    /**
     * @param name
     * @param date
     */
    public BeanWithDate(String name, Date date){
        super();
        this.name = name;
        this.date = date;
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

    /**
     * @return the date
     */
    public Date getDate(){
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date){
        this.date = date;
    }

}