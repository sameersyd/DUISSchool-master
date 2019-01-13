package com.haze.sameer.duisschool;

public class NotificationObject {

    String titleNoti, msgNoti, dateNoti;
    boolean unreadNoti;

    public NotificationObject(String titleNoti, String msgNoti, String dateNoti, boolean unreadNoti) {
        this.titleNoti = titleNoti;
        this.msgNoti = msgNoti;
        this.dateNoti = dateNoti;
        this.unreadNoti = unreadNoti;
    }

    public String getTitleNoti() {
        return titleNoti;
    }
    public String getMsgNoti() {
        return msgNoti;
    }
    public String getDateNoti() {
        return dateNoti;
    }
    public boolean isUnreadNoti() {
        return unreadNoti;
    }

}