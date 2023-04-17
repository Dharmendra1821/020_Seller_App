package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

public class DatabaseSubCategoriesModel {

    public int id;
    public String category_id;
    public String sub_name;

    public String getCategoriesname() {
        return categoriesname;
    }

    public void setCategoriesname(String categoriesname) {
        this.categoriesname = categoriesname;
    }

    private String isChecked = "NO";
    public String categoriesname;

    public DatabaseSubCategoriesModel() {
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String sub_category_id;
    public String sub_imageurl;
    public String sub_parent_id;

    public DatabaseSubCategoriesModel(int id, String subcategoryid, String subimageurl, String subparentid, String subname,
                                      String categoriesname) {
        this.id = id;
        this.sub_category_id = subcategoryid;
        this.sub_imageurl = subimageurl;
        this.sub_parent_id = subparentid;
        this.sub_name = subname;
        this.categoriesname = categoriesname;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_imageurl() {
        return sub_imageurl;
    }

    public void setSub_imageurl(String sub_imageurl) {
        this.sub_imageurl = sub_imageurl;
    }

    public String getSub_parent_id() {
        return sub_parent_id;
    }

    public void setSub_parent_id(String sub_parent_id) {
        this.sub_parent_id = sub_parent_id;
    }
}
