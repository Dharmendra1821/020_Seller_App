package standalone.eduqfix.qfixinfo.com.app.customer.model;
public class Filters
{
    private String field;

    private String value;

    private String condition_type;

    public void setField(String field){
        this.field = field;
    }
    public String getField(){
        return this.field;
    }
    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
    public void setCondition_type(String condition_type){
        this.condition_type = condition_type;
    }
    public String getCondition_type(){
        return this.condition_type;
    }
}
