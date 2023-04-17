package standalone.eduqfix.qfixinfo.com.app.customer.model;
import java.util.List;
public class Addresses
{
    private Integer id;
    private Integer customer_id;
    private Region region;
    private Integer region_id;
    private String country_id;
    private List<String> street;
    private String telephone;
    private String postcode;
    private String city;
    private String firstname;
    private String lastname;
    private boolean default_shipping;
    private boolean default_billing;
    boolean IsChecked;

    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
    public void setCustomer_id(Integer customer_id){
        this.customer_id = customer_id;
    }
    public Integer getCustomer_id(){
        return this.customer_id;
    }
    public void setRegion(Region region){
        this.region = region;
    }
    public Region getRegion(){
        return this.region;
    }
    public void setRegion_id(Integer region_id){
        this.region_id = region_id;
    }
    public Integer getRegion_id(){
        return this.region_id;
    }
    public void setCountry_id(String country_id){
        this.country_id = country_id;
    }
    public String getCountry_id(){
        return this.country_id;
    }
    public void setStreet(List<String> street){
        this.street = street;
    }
    public List<String> getStreet(){
        return this.street;
    }
    public void setTelephone(String telephone){
        this.telephone = telephone;
    }
    public String getTelephone(){
        return this.telephone;
    }
    public void setPostcode(String postcode){
        this.postcode = postcode;
    }
    public String getPostcode(){
        return this.postcode;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
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
    public void setDefault_shipping(boolean default_shipping){
        this.default_shipping = default_shipping;
    }
    public boolean getDefault_shipping(){
        return this.default_shipping;
    }
    public void setDefault_billing(boolean default_billing){
        this.default_billing = default_billing;
    }
    public boolean getDefault_billing(){
        return this.default_billing;
    }

    public boolean getChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }
}

