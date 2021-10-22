import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class response_to_get_block_from_district {

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

        @SerializedName("tehsil_name")
        @Expose
        private String tehsilName;

        public String getTehsilName() {
            return tehsilName;
        }

        public void setTehsilName(String tehsilName) {
            this.tehsilName = tehsilName;
        }

    }

}