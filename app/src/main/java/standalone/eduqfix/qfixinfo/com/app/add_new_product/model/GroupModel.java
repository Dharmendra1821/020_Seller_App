package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

public class GroupModel {


    public GroupModel(boolean isSelected, String categoryname) {
        this.isSelected = isSelected;
        this.categoryname = categoryname;
    }

    public boolean isSelected;
    public String categoryname;

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
