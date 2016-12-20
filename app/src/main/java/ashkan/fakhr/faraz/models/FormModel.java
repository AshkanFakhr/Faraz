package ashkan.fakhr.faraz.models;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ${Ashkan} on ${12/9/2016}.
 */
public class FormModel {

    String label;
    String id;
    String type;
    String name;

    @JSONField(name = "default")
    boolean isDefault;

    FormConfigurationModel configuration;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public FormConfigurationModel getConfiguration() {
        return configuration;
    }

    public void setConfiguration(FormConfigurationModel configuration) {
        this.configuration = configuration;
    }
}
