package standalone.eduqfix.qfixinfo.com.app.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 27/2/19.
 */

public class Region {

    @SerializedName("region_code")
    private String regionCode;
    @SerializedName("region")
    private String region;
    @SerializedName("region_id")
    private Integer regionId;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }
}
