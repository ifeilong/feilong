package com.feilong.excel.itemdescription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.feilong.lib.lang3.StringUtils;

public class ItemDescriptionCommand implements Serializable{

    private static final long serialVersionUID = -2760390653344564935L;

    /**
     * 商品style
     */
    private String            style;

    /**
     * 商品code
     */
    private String            itemCode;

    /**
     * 模板code
     */
    private String            templeCode;

    /**
     * 商品名称
     */
    private String            productName;

    /**
     * 商品介绍
     */
    private String            description;

    /**
     * 模特图片
     */
    private String            modelPicture;

    /**
     * 新模板中左边各种介绍
     */
    private String            leftBullet;

    /**
     * 新模板中左边各种介绍已"#"号分隔
     */
    private List<String>      leftBulletList;

    /**
     * 新模板中右边各种介绍
     */
    private String            rightBullet;

    /**
     * 新模板中右边各种介绍已"#"号分隔
     */
    private List<String>      rightBulletList;

    /**
     * 旧模板中的各种介绍
     */
    private String            bullet;

    /**
     * 新模板中各种介绍已"#"号分隔
     */
    private List<String>      bulletList;

    /**
     * 图片地址已"#"号分隔
     */
    private String            imgUrl;

    /**
     * 各种介绍转换为linklist
     */
    private List<String>      imgUrlList;

    /**
     * 视频截图地址
     */
    private String            videoImageUrl;

    /**
     * 视频地址
     */
    private String            videoUrl;

    public String getStyle(){
        return style;
    }

    public void setStyle(String style){
        this.style = style;
    }

    public String getItemCode(){
        return itemCode;
    }

    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }

    public String getTempleCode(){
        return templeCode;
    }

    public void setTempleCode(String templeCode){
        this.templeCode = templeCode;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getModelPicture(){
        return modelPicture;
    }

    public void setModelPicture(String modelPicture){
        this.modelPicture = modelPicture;
    }

    public String getLeftBullet(){
        return leftBullet;
    }

    public void setLeftBullet(String leftBullet){
        this.leftBullet = leftBullet;
    }

    public List<String> getLeftBulletList(){
        List<String> tempBulletList = new ArrayList<String>();
        if (StringUtils.isNotBlank(this.leftBullet)){
            String tempbullet = StringUtils.trim(this.leftBullet);
            if (tempbullet.contains("#")){
                String[] split = tempbullet.split("#");
                for (String string : split){
                    tempBulletList.add(string);
                }
            }else{
                tempBulletList.add(tempbullet);
            }
        }
        this.leftBulletList = tempBulletList;
        return this.leftBulletList;
    }

    public void setLeftBulletList(List<String> leftBulletList){
        this.leftBulletList = leftBulletList;
    }

    public String getRightBullet(){
        return rightBullet;
    }

    public void setRightBullet(String rightBullet){
        this.rightBullet = rightBullet;
    }

    public List<String> getRightBulletList(){
        List<String> tempBulletList = new ArrayList<String>();
        if (StringUtils.isNotBlank(this.rightBullet)){
            String tempbullet = StringUtils.trim(this.rightBullet);
            if (tempbullet.contains("#")){
                String[] split = tempbullet.split("#");
                for (String string : split){
                    tempBulletList.add(string);
                }
            }else{
                tempBulletList.add(tempbullet);
            }
        }
        this.rightBulletList = tempBulletList;
        return this.rightBulletList;
    }

    public void setRightBulletList(List<String> rightBulletList){
        this.rightBulletList = rightBulletList;
    }

    public String getBullet(){
        return bullet;
    }

    public void setBullet(String bullet){
        if (StringUtils.isNotBlank(this.bullet)){
            this.bullet = this.bullet + "#" + bullet;
        }else{
            this.bullet = bullet;
        }
    }

    public List<String> getBulletList(){
        List<String> tempBulletList = new ArrayList<String>();
        if (StringUtils.isNotBlank(this.bullet)){
            String tempbullet = StringUtils.trim(this.bullet);
            if (tempbullet.contains("#")){
                String[] split = tempbullet.split("#");
                for (String string : split){
                    tempBulletList.add(string);
                }
            }else{
                tempBulletList.add(tempbullet);
            }
        }
        this.bulletList = tempBulletList;
        return this.bulletList;
    }

    public void setBulletList(List<String> bulletList){
        this.bulletList = bulletList;
    }

    public String getImgUrl(){
        return imgUrl;
    }

    public void setImgUrl(String imgUrl){
        if (StringUtils.isNotBlank(this.imgUrl)){
            this.imgUrl = this.imgUrl + "#" + imgUrl;
        }else{
            this.imgUrl = imgUrl;
        }
    }

    public List<String> getImgUrlList(){
        List<String> tempImgUrlList = new ArrayList<String>();
        if (StringUtils.isNotBlank(this.imgUrl)){
            String tempimgUrl = StringUtils.trim(this.imgUrl);
            if (tempimgUrl.contains("#")){
                String[] split = tempimgUrl.split("#");
                for (String string : split){
                    tempImgUrlList.add(string);
                }
            }else{
                tempImgUrlList.add(tempimgUrl);
            }
        }
        this.imgUrlList = tempImgUrlList;
        return this.imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList){
        this.imgUrlList = imgUrlList;
    }

    public String getVideoImageUrl(){
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl){
        this.videoImageUrl = videoImageUrl;
    }

    public String getVideoUrl(){
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl){
        this.videoUrl = videoUrl;
    }

}
