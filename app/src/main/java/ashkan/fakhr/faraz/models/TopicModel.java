package ashkan.fakhr.faraz.models;

import java.util.List;

/**
 * Created by ${Ashkan} on ${12/9/2016}.
 */

public class TopicModel {

    String id;
    String name;
    String department;
    int priority;
    boolean isEnabled;
    boolean isActive;
    List<FormModel> form;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<FormModel> getForm() {
        return form;
    }

    public void setForm(List<FormModel> form) {
        this.form = form;
    }
}
