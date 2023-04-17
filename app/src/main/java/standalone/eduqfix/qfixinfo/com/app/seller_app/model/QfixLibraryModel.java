package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class QfixLibraryModel {

    private String imagemasterlist_id;
    private String title;
    private String image;

    public QfixLibraryModel(String imagemasterlist_id, String title, String image) {
        this.imagemasterlist_id = imagemasterlist_id;
        this.title = title;
        this.image = image;
    }

    public String getImagemasterlist_id() {
        return imagemasterlist_id;
    }

    public void setImagemasterlist_id(String imagemasterlist_id) {
        this.imagemasterlist_id = imagemasterlist_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
