package standalone.eduqfix.qfixinfo.com.app.customer.model;
public class Custom_attributes
{
    private String attribute_code;

    private String value;

    public void setAttribute_code(String attribute_code){
        this.attribute_code = attribute_code;
    }
    public String getAttribute_code(){
        return this.attribute_code;
    }
    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
