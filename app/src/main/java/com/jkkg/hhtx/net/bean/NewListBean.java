package com.jkkg.hhtx.net.bean;

import java.util.Date;

public class NewListBean {


    /**
     * information_id : 5
     * imgs : ["http://129.3.3.3:8080//zx/05/2f55eacb-2c69-4208-931f-275cd6c083bc.jpg","http://129.3.3.3:8080//zx/05/98ff3fda-b562-4b67-92e1-48c1a610e74d.jpg","http://129.3.3.3:8080//zx/05/2f8469eb-4414-4241-b5f7-fa07681edb3c.jpg","http://129.3.3.3:8080//zx/05/43ca4df1-62e3-4318-83d8-841cddb0966c.jpg"]
     * bearish : 7
     * create_time : 2020-10-14T07:45:19
     * bullish : 0
     * approval_status : 2
     * title : ttw
     * content : 请问请问我琴
     * update_time : 2020-10-14T07:45:19
     * user_id : 27779
     * review_instructions : 允许发布
     * review_time : 2020-10-14T15:46:09
     * number_of_comments : 2
     * "userAddress": "0xaasdasdad用户地址",用户地址
     * "headPortrait": "http://asfasda.jpg"用户头像
     */

    private int information_id;
    private String imgs;
    private int bearish;
    private Date create_time;
    private int bullish;
    private int approval_status;
    private String title;
    private String content;
    private Date update_time;
    private int user_id;
    private String review_instructions;
    private String review_time;
    private int number_of_comments;

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    private String headPortrait;
    private String userAddress;

    public int getInformation_id() {
        return information_id;
    }

    public void setInformation_id(int information_id) {
        this.information_id = information_id;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public int getBearish() {
        return bearish;
    }

    public void setBearish(int bearish) {
        this.bearish = bearish;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getBullish() {
        return bullish;
    }

    public void setBullish(int bullish) {
        this.bullish = bullish;
    }

    public int getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(int approval_status) {
        this.approval_status = approval_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getReview_instructions() {
        return review_instructions;
    }

    public void setReview_instructions(String review_instructions) {
        this.review_instructions = review_instructions;
    }

    public String getReview_time() {
        return review_time;
    }

    public void setReview_time(String review_time) {
        this.review_time = review_time;
    }

    public int getNumber_of_comments() {
        return number_of_comments;
    }

    public void setNumber_of_comments(int number_of_comments) {
        this.number_of_comments = number_of_comments;
    }
}
