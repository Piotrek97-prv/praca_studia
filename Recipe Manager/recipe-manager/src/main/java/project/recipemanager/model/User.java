package project.recipemanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name="user_data")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = -3924170999808980476L;


    //Pola klasy

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(name="username", unique=true)
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private boolean enabled;

    @ManyToMany(fetch=FetchType.EAGER)
    private Collection<Authority> authorities;

    @OneToOne(cascade=CascadeType.ALL)
    private ActivationCode activationCode;

    @Column
    private boolean banned = false;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", orphanRemoval = false)
    private List<Recipe> myRecipes;

    //@JsonIgnore
//    @ManyToMany(cascade = {CascadeType.ALL})
//    @JoinTable(
//            name = "User_FavoriteRecipes",
//            joinColumns = { @JoinColumn(name = "user_id") },
//            inverseJoinColumns = { @JoinColumn(name = "recipe_id") }
//    )
//    private List<Recipe> myFavoritesRecipes;

    @Column
    private long[] myFavoritesRecipes = new long[0];


    //Konstruktory

    public User() {
        this.authorities = Collections.emptyList();
    }

    public User(String email, String password, String name, String surname, boolean enabled) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
    }

    public User(String email, String password, String name, String surname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public User(String username, String password, Collection<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public User(String username, String password, Collection<Authority> authorities, String name, String surname) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.name = name;
        this.surname = surname;
    }

    public User(String username, String email, String password, String name, String surname, boolean enabled, Collection<Authority> authorities, ActivationCode activationCode, boolean banned) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.authorities = authorities;
        this.activationCode = activationCode;
        this.banned = banned;
    }

    //Getter and Setter


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }

    public ActivationCode getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(ActivationCode activationCode) {
        this.activationCode = activationCode;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public List<Recipe> getMyRecipes() {
        return myRecipes;
    }

    public void setMyRecipes(List<Recipe> myRecipes) {
        this.myRecipes = myRecipes;
    }

    public long[] getMyFavoritesRecipes() {
        return myFavoritesRecipes;
    }

    public void setMyFavoritesRecipes(long[] myFavoritesRecipes) {
        this.myFavoritesRecipes = myFavoritesRecipes;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
