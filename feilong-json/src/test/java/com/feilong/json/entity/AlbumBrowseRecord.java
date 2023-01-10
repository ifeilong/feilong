package com.feilong.json.entity;

public class AlbumBrowseRecord{

    private Long albumId;

    private Long trackId;

    private Long browsedAt;

    /**
     * @return the albumId
     */
    public Long getAlbumId(){
        return albumId;
    }

    /**
     * @param albumId
     *            the albumId to set
     */
    public void setAlbumId(Long albumId){
        this.albumId = albumId;
    }

    /**
     * @return the trackId
     */
    public Long getTrackId(){
        return trackId;
    }

    /**
     * @param trackId
     *            the trackId to set
     */
    public void setTrackId(Long trackId){
        this.trackId = trackId;
    }

    /**
     * @return the browsedAt
     */
    public Long getBrowsedAt(){
        return browsedAt;
    }

    /**
     * @param browsedAt
     *            the browsedAt to set
     */
    public void setBrowsedAt(Long browsedAt){
        this.browsedAt = browsedAt;
    }

}