package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SMSContactModel {

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @SerializedName("list_id")
    private String list_id;
    private boolean isSelected;
    public SMSContactModel(String list_id, String listname, List<SMSContactListData> listdata) {
        this.list_id = list_id;
        this.listname = listname;
        this.listdata = listdata;
    }

    @SerializedName("listname")
    private String listname;

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public List<SMSContactListData> getListdata() {
        return listdata;
    }

    public void setListdata(List<SMSContactListData> listdata) {
        this.listdata = listdata;
    }

    @SerializedName("listdata")
    public List<SMSContactListData> listdata;


}
