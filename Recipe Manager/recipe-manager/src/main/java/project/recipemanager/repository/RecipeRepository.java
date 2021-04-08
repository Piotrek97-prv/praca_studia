package project.recipemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.recipemanager.model.Recipe;
import project.recipemanager.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAll();
    Optional<List<Recipe>> findByAuthor(User user);

}
