package com.demo.kotlin.bean;

public class LeToBean {
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

    @Override
    public String toString() {
        return "LeToBean{" +
                "blueBall=" + blueBall +
                ", redBall=" + redBall +
                '}';
    }


}
