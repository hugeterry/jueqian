package us.xingkong.jueqian.bean.ForumBean.BombBean;


import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Garfield on 1/11/17.
 */

public class _User extends BmobObject {

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public BmobFile getProfile() {
        return profile;
    }

    public void setProfile(BmobFile profile) {
        this.profile = profile;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getACCOUNT() {
        return ACCOUNT;
    }

    public void setACCOUNT(String ACCOUNT) {
        this.ACCOUNT = ACCOUNT;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    private String username;
    private String ACCOUNT;//账号
    private Boolean gender;//性别
    private BmobFile profile;//头像
    private String selfIntro;//自我介绍
    private Integer state;//用户状态

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getBoy() {
        return isBoy;
    }

    public void setBoy(Boolean boy) {
        isBoy = boy;
    }

    public BmobFile getProfile() {
        return profile;
    }

    public void setProfile(BmobFile profile) {
        this.profile = profile;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<String> getQuestionsID() {
        return questionsID;
    }

    public void setQuestionsID(List<String> questionsID) {
        this.questionsID = questionsID;
    }

    public List<String> getCollectionsID() {
        return collectionsID;
    }

    public void setCollectionsID(List<String> collectionsID) {
        this.collectionsID = collectionsID;
    }
}
