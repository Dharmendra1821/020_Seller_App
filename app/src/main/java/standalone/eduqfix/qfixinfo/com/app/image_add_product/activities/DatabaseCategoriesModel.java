package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

public class DatabaseCategoriesModel {

    public int id;
    public String category_id;
    public String name;
    public String imageurl;
    public String category_banner;
    public String parent_id;

    public DatabaseCategoriesModel() {

    }

    public DatabaseCategoriesModel(int id, String category_id, String name, String imageurl, String category_banner, String parent_id) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.imageurl = imageurl;
        this.category_banner = category_banner;
        this.parent_id = parent_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
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

    public String getCategory_banner() {
        return category_banner;
    }

    public void setCategory_banner(String category_banner) {
        this.category_banner = category_banner;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
