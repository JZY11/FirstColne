package com.btb.zb;

/**
 * Created by zhenya.1291813139.com
 * on 2018/1/15.
 * btb_grab.
 */
public class SubModel {
    private String channel;
    private String event="addChannel";

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
