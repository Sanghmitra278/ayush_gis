package com.example.ayush_gis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



    public class response_to_get_office_type_from_ayush_stream_and_dis {

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

            @SerializedName("office_category")
            @Expose
            private String officeCategory;

            public String getOfficeCategory() {
                return officeCategory;
            }

            public void setOfficeCategory(String officeCategory) {
                this.officeCategory = officeCategory;
            }

        }

    }




