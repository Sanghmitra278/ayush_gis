package com.example.ayush_gis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class response_to_get_ayush_type_list {
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

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

        @SerializedName("ayush_stream")
        @Expose
        private String ayush_type;

        public String get_ayush_type() {
            return ayush_type;
        }

        public void setDistrictName(String districtName) {
            this.ayush_type = districtName;
        }

    }
}
