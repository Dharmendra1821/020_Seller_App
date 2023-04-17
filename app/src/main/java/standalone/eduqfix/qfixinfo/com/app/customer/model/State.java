package standalone.eduqfix.qfixinfo.com.app.customer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 26/4/19.
 */

public class State {

    @SerializedName("id")
    private String regionId;
    @SerializedName("country_id")
    private String countryId;
    @SerializedName("name")
    private String name;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
