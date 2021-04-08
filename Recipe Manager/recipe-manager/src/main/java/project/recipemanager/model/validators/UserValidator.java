package project.recipemanager.model.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import project.recipemanager.model.User;
import project.recipemanager.repository.UserRepository;

/*
 * Sprawdzenie danych użytkownika wprowadzonych z formularze rejestracyjnego, wg określonych kryteriów.
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean supports(Class<?> clazz) { return UserValidator.class.equals(clazz); }

    @Override
    public void validate(Object obj, Errors errors) {
        User user = (User) obj;

        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            errors.reject("required", "Nie wszystkie wymagane pola zostały wprowadzone");
            return;
        }

        String uName = user.getUsername();
        if (userRepo.existsByUsername(uName)) errors.reject("username", "Istnieje już użytkownik o takiej nazwie.");
        if (uName.length() == 0) errors.reject("username", "Nazwa użytkownika nie może być pusta.");
        if (uName.length() < 4 || uName.length() > 16) errors.reject("username", "Nazwa uzytkownika nie może być krótsza od 4 znaków i dłuższa niż 16.");

        String pass = user.getPassword();
        if (pass.length() == 0) errors.reject("password", "Hasło nie może być puste.");
        if (pass.length() < 6 || pass.length() > 16 ) errors.reject("password", "Hasło musi zawierać conajmniej 6 i maksymalnie 16 znaków.");

        String email = user.getEmail();
        if (email.length() == 0) errors.reject("e-mail", "Email nie może byc pusty.");
        if (!email.contains("@")) errors.reject("e-mail", "Email musi zawierać @.");
        if (email.length() < 3) errors.reject("e-mail", "Email jest za krótki.");
    }
}
