package project.recipemanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="recipe_data")
public class Recipe implements Serializable {

    private static final long serialVersionUID = -3924170999808980476L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    //@JsonIgnore
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;


    public Recipe() {
    }

    public Recipe(String title, String description, User author) {
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public Recipe(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Recipe(long id, String title, String description, User author, List<Ingredient> ingredients) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.ingredients = ingredients;
    }

    public Recipe(String title, String description, List<Ingredient> ingredients) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
    }

    public Recipe(long id, String title, String description, List<Ingredient> ingredients) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
    }

    public Recipe(String title, String description, User author, List<Ingredient> ingredients) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.ingredients = ingredients;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", ingredients=" + ingredients +
                '}';
    }
}
