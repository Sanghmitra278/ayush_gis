//response for login page

package com.example.ayush_gis;

public class admin_login_response {

    private boolean status;
    private String msg;


    public boolean isStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
