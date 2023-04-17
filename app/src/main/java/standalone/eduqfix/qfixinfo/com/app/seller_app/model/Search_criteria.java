package standalone.eduqfix.qfixinfo.com.app.seller_app.model;
import java.util.List;
public class Search_criteria
{
    private List<Filter_groups> filter_groups;

    public void setFilter_groups(List<Filter_groups> filter_groups){
        this.filter_groups = filter_groups;
    }
    public List<Filter_groups> getFilter_groups(){
        return this.filter_groups;
    }
}
