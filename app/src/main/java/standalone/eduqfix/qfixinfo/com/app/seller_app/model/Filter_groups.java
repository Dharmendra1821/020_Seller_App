package standalone.eduqfix.qfixinfo.com.app.seller_app.model;
import java.util.List;
public class Filter_groups
{
    private List<Filters> filters;

    public void setFilters(List<Filters> filters){
        this.filters = filters;
    }
    public List<Filters> getFilters(){
        return this.filters;
    }
}

