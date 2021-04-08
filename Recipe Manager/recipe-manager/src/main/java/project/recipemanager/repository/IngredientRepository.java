package project.recipemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.recipemanager.model.Ingredient;
import project.recipemanager.model.Recipe;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAll();

    Optional<List<Ingredient>> findByRecipe(Recipe recipe);
}
