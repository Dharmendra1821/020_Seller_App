package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class SMSContactListData   {

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @SerializedName("id")
    private String id;
    private String listSize;
    private boolean isSelected;
    public String getListSize() {
        return listSize;
    }

    public void setListSize(String listSize) {
        this.listSize = listSize;
    }

    public SMSContactListData(String id, String list_id, String name, String mobile, String emailid
    ,String listSize) {
        this.id = id;
        this.list_id = list_id;
        this.name = name;
        this.mobile = mobile;
        this.emailid = emailid;
        this.listSize = listSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    @SerializedName("list_id")
    private String list_id;
    @SerializedName("name")
    private String name;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("emailid")
    private String emailid;



}
