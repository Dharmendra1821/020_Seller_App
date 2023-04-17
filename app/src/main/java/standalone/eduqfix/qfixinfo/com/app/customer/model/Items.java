package standalone.eduqfix.qfixinfo.com.app.customer.model;
import java.util.List;
public class Items
{
    private Integer id;
    private Integer group_id;
    private String default_billing;
    private String default_shipping;
    private String created_at;
    private String updated_at;
    private String created_in;
    private String email;
    private String firstname;
    private String lastname;
    private Integer gender;
    private Integer store_id;
    private Integer website_id;
    private List<Addresses> addresses;

    public List<Custom_attributes> getCustom_attributes() {
        return custom_attributes;
    }

    public void setCustom_attributes(List<Custom_attributes> custom_attributes) {
        this.custom_attributes = custom_attributes;
    }

    private Integer disable_auto_group_change;
    private List<Custom_attributes> custom_attributes;

    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
    public void setGroup_id(Integer group_id){
        this.group_id = group_id;
    }
    public Integer getGroup_id(){
        return this.group_id;
    }
    public void setDefault_billing(String default_billing){
        this.default_billing = default_billing;
    }
    public String getDefault_billing(){
        return this.default_billing;
    }
    public void setDefault_shipping(String default_shipping){
        this.default_shipping = default_shipping;
    }
    public String getDefault_shipping(){
        return this.default_shipping;
    }
    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }
    public String getCreated_at(){
        return this.created_at;
    }
    public void setUpdated_at(String updated_at){
        this.updated_at = updated_at;
    }
    public String getUpdated_at(){
        return this.updated_at;
    }
    public void setCreated_in(String created_in){
        this.created_in = created_in;
    }
    public String getCreated_in(){
        return this.created_in;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    public String getFirstname(){
        return this.firstname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public String getLastname(){
        return this.lastname;
    }
    public void setGender(Integer gender){
        this.gender = gender;
    }
    public Integer getGender(){
        return this.gender;
    }
    public void setStore_id(Integer store_id){
        this.store_id = store_id;
    }
    public Integer getStore_id(){
        return this.store_id;
    }


    public void setWebsite_id(Integer website_id){
        this.website_id = website_id;
    }
    public Integer getWebsite_id(){
        return this.website_id;
    }

    public List<Addresses> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Addresses> addresses) {
        this.addresses = addresses;
    }

    public void setDisable_auto_group_change(Integer disable_auto_group_change){
        this.disable_auto_group_change = disable_auto_group_change;
    }
    public Integer getDisable_auto_group_change(){
        return this.disable_auto_group_change;
    }

}
