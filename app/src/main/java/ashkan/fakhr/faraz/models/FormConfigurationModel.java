package ashkan.fakhr.faraz.models;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by ${Ashkan} on ${12/9/2016}.
 */
public class FormConfigurationModel {

    List<ChoiceModel> choices;

    @JSONField(name = "default")
    String isDefault;

    String prompt;
    boolean multiselect;

    public List<ChoiceModel> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceModel> choices) {
        this.choices = choices;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public boolean isMultiselect() {
        return multiselect;
    }

    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }
}
