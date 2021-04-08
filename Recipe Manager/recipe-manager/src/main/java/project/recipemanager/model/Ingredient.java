package project.recipemanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="ingredient_data")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @Column
    private String name;

    @Column
    private String quantity;

    @ManyToOne(cascade = CascadeType.PERSIST)//(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Ingredient(String name, String quantity, Recipe recipe) {
        this.name = name;
        this.quantity = quantity;
        this.recipe = recipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
