package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddNewProductMediaGallery implements Serializable {


    public ArrayList<AddNewProductImages> images;

    public ArrayList<AddNewProductImages> getImages() {
        return images;
    }

    public void setImages(ArrayList<AddNewProductImages> images) {
        this.images = images;
    }
}
