package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtentionAttributes {

    @SerializedName("configurable_item_options")
    private List<ConfigurableItemOption> configurable_item_options = null;

    public List<ConfigurableItemOption> getConfigurable_item_options() {
        return configurable_item_options;
    }

    public void setConfigurable_item_options(List<ConfigurableItemOption> configurable_item_options) {
        this.configurable_item_options = configurable_item_options;
    }
}
