package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class SubCategoryModel {

   private String categoryId;
   private String name;
   private String imageurl;
   private String parent_id;

    public SubCategoryModel(String categoryId, String name, String imageurl, String parent_id) {
        this.categoryId = categoryId;
        this.name = name;
        this.imageurl = imageurl;
        this.parent_id = parent_id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
