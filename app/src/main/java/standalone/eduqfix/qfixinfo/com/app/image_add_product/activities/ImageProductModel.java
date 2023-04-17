package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

public class ImageProductModel {

    private String valueId;
    private String file;
    private String entityId;
    private String flag;

    public ImageProductModel(String valueId, String file, String entityId,String flag) {
        this.valueId = valueId;
        this.file = file;
        this.entityId = entityId;
        this.flag = flag;
    }

    public String getValueId() {
        return valueId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
