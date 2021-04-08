package project.recipemanager.model.validators;

import java.util.Map;

public class UserValidationErrors {

    private Map<String, String> errors;

    public UserValidationErrors() {
    }

    public UserValidationErrors(Map<String, String> errors) {
        this.errors = errors;
    }



    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
