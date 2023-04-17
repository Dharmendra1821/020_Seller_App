package standalone.eduqfix.qfixinfo.com.app.seller_app.community.model;

public class ImageModel {
    private String images;
    private String baseUrl;


    public ImageModel(){

    }
    public ImageModel(String images,String baseUrl) {
        this.images = images;
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
