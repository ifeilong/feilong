package com.feilong.core.lang.stringutil;

import com.feilong.lib.lang3.builder.EqualsBuilder;
import com.feilong.lib.lang3.builder.HashCodeBuilder;
import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * 查询参数.
 */
public class TrackQueryExtendParam{

    /** 单集编号 最小. */
    private Integer minEpisodeNumber;

    /** 单集编号 最大. */
    private Integer maxEpisodeNumber;

    //---------------------------------------------------------------

    /**
     * 
     */
    public TrackQueryExtendParam(){
        super();
    }

    /**
     * @param minEpisodeNumber
     * @param maxEpisodeNumber
     */
    public TrackQueryExtendParam(Integer minEpisodeNumber, Integer maxEpisodeNumber){
        super();
        this.minEpisodeNumber = minEpisodeNumber;
        this.maxEpisodeNumber = maxEpisodeNumber;
    }

    //---------------------------------------------------------------

    /**
     * To string.
     *
     * @return the string
     */
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    /**
     * Gets the 单集编号 最小.
     *
     * @return the 单集编号 最小
     */
    public Integer getMinEpisodeNumber(){
        return minEpisodeNumber;
    }

    /**
     * Sets the 单集编号 最小.
     *
     * @param minEpisodeNumber
     *            the new 单集编号 最小
     */
    public void setMinEpisodeNumber(Integer minEpisodeNumber){
        this.minEpisodeNumber = minEpisodeNumber;
    }

    /**
     * Gets the 单集编号 最大.
     *
     * @return the 单集编号 最大
     */
    public Integer getMaxEpisodeNumber(){
        return maxEpisodeNumber;
    }

    /**
     * Sets the 单集编号 最大.
     *
     * @param maxEpisodeNumber
     *            the new 单集编号 最大
     */
    public void setMaxEpisodeNumber(Integer maxEpisodeNumber){
        this.maxEpisodeNumber = maxEpisodeNumber;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj){
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}