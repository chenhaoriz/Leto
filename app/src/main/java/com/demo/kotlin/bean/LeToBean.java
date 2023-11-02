package com.demo.kotlin.bean;

import java.util.UUID;

public class LeToBean {
    public String id = getId(); // 生成包含字母和数字的10位随机字符串

    private BlueBall blueBall;
    private RedBall redBall;

    public BlueBall getBlueBall() {
        return blueBall;
    }

    public void setBlueBall(BlueBall blueBall) {
        this.blueBall = blueBall;
    }

    public RedBall getRedBall() {
        return redBall;
    }

    public void setRedBall(RedBall redBall) {
        this.redBall = redBall;
    }

    public LeToBean(RedBall redBall, BlueBall blueBall) {
        this.blueBall = blueBall;
        this.redBall = redBall;
    }

    public LeToBean(String id, RedBall redBall, BlueBall blueBall) {
        this.blueBall = blueBall;
        this.redBall = redBall;
        this.id = id;
    }

    @Override
    public String toString() {
        return "LeToBean{" +
                "blueBall=" + blueBall +
                ", redBall=" + redBall +
                '}';
    }

    private String getId() {
        UUID randomUUID = UUID.randomUUID();
        String randomUUIDString = randomUUID.toString();
        return randomUUIDString.substring(0, 8);
    }

}
