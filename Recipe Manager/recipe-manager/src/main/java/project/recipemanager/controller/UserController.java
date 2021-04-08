package project.recipemanager.controller;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import project.recipemanager.model.Authority;
import project.recipemanager.model.User;
import project.recipemanager.model.validators.UserValidationErrors;
import project.recipemanager.model.validators.UserValidator;
import project.recipemanager.repository.AuthorityRepository;
import project.recipemanager.repository.UserRepository;
import project.recipemanager.security.AuthenticationRequest;
import project.recipemanager.security.AuthenticationResponse;
import project.recipemanager.security.JWTUtil;
import project.recipemanager.services.EmailService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    private Logger log = Logger.getLogger(UserValidator.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthorityRepository authRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private EmailService emailService;


    /*
     * Metoda do rejestracji użytkownika. Waliduje jego pola (klasa UserValidator), dodaje role "ROLE_USER", koduje hasło i domyślnie
     * ustawia stan konta jako "nieaktywne".
     * @param user Obiekt użytkownika przechwycony jako JSON i przekonwertowany na obiekt
     * @param errors Rejestr błędów automatycznie inicjalizowany przez Spring'a
     * @return UserValidationErrors Zwraca obiekt zawierający Mapę -> (Kod błędu, Wiadomość błędu). Gdy błędy walidacji nie występują zwraca
     * pustą mapę
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, BindingResult errors) {
        userValidator.validate(user, errors);

        if (errors.hasErrors()) {
            Map<String, String> validErrors = new HashMap<>(errors.getAllErrors().size());
            for(ObjectError error : errors.getAllErrors()) {
                validErrors.put(error.getCode(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new UserValidationErrors(validErrors));
        }

        Authority userRole = authRepo.findByAuthority("ROLE_USER");

        Collection<Authority> roles = new HashSet<Authority>();
        roles.add(userRole);
        user.setAuthorities(roles);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        User savedUser = userRepo.save(user);
        log.info("User registered.");

        emailService.sendActivationEmailToUser(savedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /*
     * Metoda służąca do logowania. Sprawdza login i hasło za pomocą customowego Authentication Managera,
     * nastepnie dodaje je do kontextu security Spring'a i generuje token dla użytkownika.
     * @param request Opakowane dane do logowania (username/password)
     * @return ResponseEntity<?> Zwraca token opakowany w AuthenticationResponse i dołącza do niego status Http
     */
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) {
        Authentication auth = null;

        try {
            auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException except) {
            log.error("Error during user authentication");
            return ResponseEntity.badRequest().body(except.getMessage());
        }

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(JWTUtil.generateToken(userDetails)));
    }


    /*
     * Metoda odświeżająca token (przedłużenie czasu wygaśnięcia tokenu)
     * @param request Zapytanie z tokenem w headerze
     * @return Zwraca token opakowany w AuthenticationResponse i dołącza do niego status Http
     */
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = JWTUtil.trimToken(request.getHeader(JWTUtil.HEADER));

        String newToken = null;
        if (JWTUtil.canTokenBeRefreshed(token)) {
            newToken = JWTUtil.refreshToken(token);
            log.info("Tocken refreshed.");
        } else {
            log.error("Token cannot be refreshed.");
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(new AuthenticationResponse(newToken));
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        System.out.println("Get all Users...");
        List<User> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users;
    }

    @GetMapping("/users/me")
    public User getMe() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Obiekt userDetails z getMe" + userDetails.getAuthorities());
        User user = (User) userDetails;
        Optional<User> u = userRepo.findById(user.getId());
        if (u.isPresent()) {
            return user;
        } else {
            User u2 = new User();
            u2.setName("");
            u2.setSurname("");
            return u2;
        }
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable("id") long id) {
        System.out.println("User with ID = " + id);
        Optional<User> user = userRepo.findById(id);
        return user;
    }


    /*
     * Kontroler do którego dostęp ma jedynie administrator.
     */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String securedAdmin() {
        return new String("Dane do których dostęp ma jedynie administrator.");
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/users/ban/{id}")
    public ResponseEntity<User> banUser(@PathVariable("id") long id) {
        System.out.println("Ban User with ID = " + id);
        Optional<User> userData = userRepo.findById(id);
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setBanned(!_user.isBanned());
            return new ResponseEntity<>(userRepo.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        //Optional<User> user = userRepo.findById(1L);
        if(id == 1) {
            return new ResponseEntity<>("User has not been deleted!", HttpStatus.OK);
        }

        System.out.println("Delete User with ID = " + id);
        userRepo.deleteById(id);
        return new ResponseEntity<>("User has been deleted!", HttpStatus.OK);
    }




}
