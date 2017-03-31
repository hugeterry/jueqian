package us.xingkong.jueqian.bean.ForumBean.BombBean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by lenovo on 2017/1/13.
 */

public class _User extends BmobUser {
    private String nickname; //昵称
    private String selfsign;//个性签名
    private String gender;//性别
    private String profile;//头像
    private Integer state;//用户状态
    private BmobRelation collections;//我的收藏列表
    private BmobRelation questions;//我的问题列表
    private BmobRelation answers;//我的回答列表
    private BmobRelation recentlooks;//最近浏览列表
    private BmobRelation comments;//回复我的列表

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BmobRelation getComments() {
        return comments;
    }

    public void setComments(BmobRelation comments) {
        this.comments = comments;
    }

    public BmobRelation getRecentlooks() {
        return recentlooks;
    }

    public void setRecentlooks(BmobRelation recentlooks) {
        this.recentlooks = recentlooks;
    }

    public BmobRelation getAnswers() {
        return answers;
    }

    public void setAnswers(BmobRelation answers) {
        this.answers = answers;
    }

    public BmobRelation getQuestions() {
        return questions;
    }

    public void setQuestions(BmobRelation questions) {
        this.questions = questions;
    }

    public BmobRelation getCollections() {
        return collections;
    }

    public void setCollections(BmobRelation collections) {
        this.collections = collections;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getSelfsign() {
        return selfsign;
    }

    public void setSelfsign(String selfsign) {
        this.selfsign = selfsign;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
