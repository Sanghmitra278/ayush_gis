package com.example.ayush_gis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


///////////////////////////////////////////////////


public class response_to_login {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public class Datum {

        @SerializedName("user_mob_num")
        @Expose
        private String userMobNum;
        @SerializedName("uprsac_division")
        @Expose
        private String uprsacDivision;
        @SerializedName("username")
        @Expose
        private String username;

        public String getUserMobNum() {
            return userMobNum;
        }

        public void setUserMobNum(String userMobNum) {
            this.userMobNum = userMobNum;
        }

        public String getUprsacDivision() {
            return uprsacDivision;
        }

        public void setUprsacDivision(String uprsacDivision) {
            this.uprsacDivision = uprsacDivision;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }
}