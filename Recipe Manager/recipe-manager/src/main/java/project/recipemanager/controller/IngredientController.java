package project.recipemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.recipemanager.model.Ingredient;
import project.recipemanager.model.Recipe;
import project.recipemanager.model.User;
import project.recipemanager.repository.IngredientRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/ingredients/create", consumes = {"application/json"}, produces = {"application/json"})
    public Ingredient createRecipe(@RequestBody Ingredient ingredient) {
        if(ingredient instanceof Ingredient) {
            System.out.println("Create new ingredient...");
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userDetails.getUsername();
            Ingredient _ingredient = ingredientRepository.save(new Ingredient(ingredient.getName(), ingredient.getQuantity()));
            System.out.println(_ingredient.getName());
            return _ingredient;
        } else {
            return null;
        }
    }


}
