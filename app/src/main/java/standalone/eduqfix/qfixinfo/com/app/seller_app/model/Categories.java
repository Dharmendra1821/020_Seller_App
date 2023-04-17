package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class Categories {


    private Integer category_id;
    private String name;
    private String imageurl;
    String category_banner;
    private String parent_id;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }


    public String getCategory_banner() {
        return category_banner;
    }

    public void setCategory_banner(String category_banner) {
        this.category_banner = category_banner;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
