package project.recipemanager.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.recipemanager.model.ImageModel;
import project.recipemanager.model.Ingredient;
import project.recipemanager.model.Recipe;
import project.recipemanager.model.User;
import project.recipemanager.repository.ImageRepository;
import project.recipemanager.repository.IngredientRepository;
import project.recipemanager.repository.RecipeRepository;
import project.recipemanager.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/find-recipe/{template}")
    public List<Recipe> findRecipe(@PathVariable("template") String template){
        System.out.println("Find recipe with template: " + template);
        String actualTemplate = template.trim();
        String[] wordsOfTemplate = actualTemplate.split(" ");
        System.out.println("Words of template: " + wordsOfTemplate);

        if(template.length() > 2) {

            List<Recipe> allRecipes = recipeRepository.findAll();
            HashSet<Recipe> recipesWithTemplateInTitle = new HashSet<>();
            HashSet<Recipe> recipesWithTemplateInDescription = new HashSet<>();
            HashSet<Recipe> recipesWithTemplateInIngredients = new HashSet<>();
            List<Recipe> finalRecipesList = new ArrayList<>();

            for (String word : wordsOfTemplate) {

                //Filtrowanie tytułów
                for (Recipe recipe : allRecipes) {
                    if(recipe.getTitle().equalsIgnoreCase(word)) {
                        recipesWithTemplateInTitle.add(recipe);
                    } else if(StringUtils.containsIgnoreCase(recipe.getTitle(), word)) {
                        recipesWithTemplateInTitle.add(recipe);
                    }
                }

                //Filtrowanie opisów
                for (Recipe recipe : allRecipes) {
                    if(StringUtils.containsIgnoreCase(recipe.getDescription(), word)) {
                        recipesWithTemplateInDescription.add(recipe);
                    }
                }

                //Filtrowanie składników
                for (Recipe recipe : allRecipes) {
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        if(ingredient.getName().equalsIgnoreCase(word)) {
                            recipesWithTemplateInIngredients.add(recipe);
                        } else if(StringUtils.containsIgnoreCase(ingredient.getName(), word)) {
                            recipesWithTemplateInIngredients.add(recipe);
                        }
                    }
                }

            }

            List<Recipe> listRecipesByTitle = new ArrayList<>(recipesWithTemplateInTitle);
            List<Recipe> listRecipesByDescription = new ArrayList<>(recipesWithTemplateInDescription);
            List<Recipe> listRecipesByIngredients = new ArrayList<>(recipesWithTemplateInIngredients);

            finalRecipesList.addAll(listRecipesByTitle);
            finalRecipesList.addAll(listRecipesByDescription);
            finalRecipesList.addAll(listRecipesByIngredients);

            System.out.println("Liczba znalezionych przepisów: " + finalRecipesList.size());
            System.out.println("Przepisy: " + finalRecipesList.toString());
            if (finalRecipesList.size() > 0) {
                return finalRecipesList;
            }

        }

        return new ArrayList<>();
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/get-ingredients/{id}")
    public List<Ingredient> getIngredients(@PathVariable("id") long id){
        System.out.println("Get Ingredients of recipe with id: " + id);
        Optional<Recipe> _recipe = recipeRepository.findById(id);
        if(_recipe.isPresent()) {
            Recipe recipe = _recipe.get();
            System.out.println(recipe.getIngredients().toString());
            return recipe.getIngredients();
        }

        return new ArrayList<>();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/recipe-of-day")
    public Recipe getRecipeOfTheDay(){
        System.out.println("Metoda getRecipeOfTheDay: ");
        List<Recipe> allRecipes = recipeRepository.findAll();
        LocalDate localDate1 = LocalDate.now();
        if(allRecipes.size() != 00  && allRecipes.size() <= 366) {
            System.out.println("if allRecipes.size() != 00  && allRecipes.size() <= 366");
            Optional<Recipe> _recipe = recipeRepository.findById((long) localDate1.getDayOfYear());
            if(_recipe.isPresent()) {
                return _recipe.get();
            }
            if(allRecipes.size() <= 31) {
                System.out.println("if allRecipes.size() <= 31");
                Optional<Recipe> _recipe2 = recipeRepository.findById((long) localDate1.getDayOfMonth());
                if(_recipe2.isPresent()) {
                    return _recipe2.get();
                }
                if(allRecipes.size() <= 7) {
                    System.out.println("if allRecipes.size() <= 7");
                    Optional<Recipe> _recipe3 = recipeRepository.findById((long) localDate1.getDayOfWeek().getValue());
                    if(_recipe3.isPresent()) {
                        return _recipe3.get();
                    }
                }
            }
        }
        Optional<Recipe> _recipe4 = recipeRepository.findById((long) allRecipes.size());
        System.out.println("przed ostatnim ifem");
        if(_recipe4.isPresent()) {
            Recipe recipe = _recipe4.get();
            return recipe;
        }

        return new Recipe("", "");
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/first-five-recipes")
    public List<Recipe> getFirstFiveRecipes(){
        List<Recipe> allRecipes = recipeRepository.findAll();
        if(allRecipes.size() >= 5) {
            Collections.reverse(allRecipes);
            List<Recipe> firstFive = allRecipes.subList(0,5);
            return firstFive;
        }

        return recipeRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/first-nine-recipes")
    public List<Recipe> getFirstNineRecipes(){
        List<Recipe> allRecipes = recipeRepository.findAll();
        if(allRecipes.size() >= 14) {
            Collections.reverse(allRecipes);
            List<Recipe> firstNine = allRecipes.subList(5,14);
            return firstNine;
        }

        return recipeRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/second-nine-recipes")
    public List<Recipe> getSecondNineRecipes(){
        List<Recipe> allRecipes = recipeRepository.findAll();
        if(allRecipes.size() >= 23) {recipeRepository.findAll();
            Collections.reverse(allRecipes);
            List<Recipe> secondNine = allRecipes.subList(14,23);
            return secondNine;
        }

        return recipeRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/is-in-favorites/{id}")
    public boolean isInFavorites(@PathVariable("id") long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Get list of ids of favorites recipes that their owner is user: " + userDetails.getUsername() + "...");
        User user = (User)userDetails;
        long[] listOfFavorites = user.getMyFavoritesRecipes();
        for(int i=0; i<listOfFavorites.length; i++) {
            if(listOfFavorites[i] == id) {
                return true;
            }
        }
        return false;
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/my-favorites-recipes-ids")
    public List<Long> getMyFavoritesRecipesIds(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Get list of ids of favorites recipes that their owner is user: " + userDetails.getUsername() + "...");
        User user = (User)userDetails;
        long[] listOfFavorites = user.getMyFavoritesRecipes();
        List<Long> response = new ArrayList<>();
        for(int i=0; i<listOfFavorites.length; i++) {
            Long element = new Long(listOfFavorites[i]);
            response.add(element);
        }
        return response;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/recipes/remove-from-favorites/{id}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable("id") long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User)userDetails;

        long[] userFavoritesRecipes = user.getMyFavoritesRecipes();
        long[] newFavoritesRecipes = new long[userFavoritesRecipes.length];
        for(int i=0; i<userFavoritesRecipes.length; i++) {
            if(userFavoritesRecipes[i] != id) {
                newFavoritesRecipes[i] = userFavoritesRecipes[i];
            }
        }

        user.setMyFavoritesRecipes(newFavoritesRecipes);
        userRepository.save(user);

        return new ResponseEntity<>("Recipe has been deleted!",HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/my-favorites-recipes")
    public List<Recipe> getMyFavoritesRecipes(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Get list of favorites recipes that their owner is user: " + userDetails.getUsername() + "...");
        User user = (User)userDetails;

        long[] listOfFavorites = user.getMyFavoritesRecipes();
        System.out.println("Length of FavRecipeList: " + listOfFavorites.length);
        List<Recipe> responseList = new ArrayList<>();
        for(int i=0; i<listOfFavorites.length; i++) {
            Optional<Recipe> _recipe = recipeRepository.findById(listOfFavorites[i]);
            if(_recipe.isPresent()) {
                Recipe recipe = _recipe.get();
                responseList.add(recipe);
            }
        }

        return responseList;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/recipes/add-to-favorites/{id}")
    public ResponseEntity<String> addToFavorites(@PathVariable("id") long id){
        System.out.println("Add to favorites Recipe with ID = " + id + " ...");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User)userDetails;

        long[] myFavorites = user.getMyFavoritesRecipes();
        for (int i=0; i<myFavorites.length; i++) {
            if(myFavorites[i] == id) {
                return new ResponseEntity<>("Recipe has been added!",HttpStatus.OK);
            }
        }

        long[] newMyFavorites = new long[myFavorites.length + 1];
        for (int i=0; i<myFavorites.length; i++) {
            newMyFavorites[i] = myFavorites[i];
        }
        newMyFavorites[myFavorites.length] = id;
        user.setMyFavoritesRecipes(newMyFavorites);

        userRepository.save(user);

        return new ResponseEntity<>("Recipe has been added!",HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(value = "/recipes/update/{id}")
    public ResponseEntity<Recipe>  updateRecipe(@PathVariable("id") long id, @RequestBody Recipe updatedRecipe){
        System.out.println("Update Recipe with ID = " + id + "...");
        System.out.println("Przekazany obiekt: " + updatedRecipe.toString());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User)userDetails;

        Optional<Recipe> _recipe = recipeRepository.findById(id);
        System.out.println("_recipe: " + _recipe.toString());
        if (_recipe.isPresent()){

            Recipe recipe = _recipe.get();
            System.out.println("Recipe recipe = _recipe.get();: " + recipe.toString());

            recipe.setTitle(updatedRecipe.getTitle());
            System.out.println("recipe.setTitle(updatedRecipe.getTitle());: " + recipe.getTitle());
            recipe.setDescription(updatedRecipe.getDescription());
            System.out.println("recipe.setDescription(updatedRecipe.getDescription());: " + recipe.getDescription());
            recipe.setAuthor(user);
            System.out.println("recipe.setAuthor(user);: " + recipe.getAuthor().toString());

            List<Ingredient> ingredientsList = recipe.getIngredients();

            System.out.println("updatedRecipe.getIngredients(): " + updatedRecipe.getIngredients());
            recipe.setIngredients(updatedRecipe.getIngredients());


            recipeRepository.save(recipe);

            for (Ingredient i : ingredientsList) {
                System.out.println("Usuwam składnik: " + i.toString());
                ingredientRepository.deleteById(i.getId());
            }

            for (Ingredient i : updatedRecipe.getIngredients()) {
                Ingredient _i = new Ingredient();
                _i.setId(i.getId());
                _i.setName(i.getName());
                _i.setQuantity(i.getQuantity());
                _i.setRecipe(recipe);
                ingredientRepository.save(_i);
            }

            return new ResponseEntity<>(recipe, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/recipes/delete/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Delete Recipe with ID = " + id + " ...");

        Recipe recipe = new Recipe();

        Optional<Recipe> _recipe = recipeRepository.findById(id);
        if (_recipe.isPresent()) {
            recipe = _recipe.get();
            Optional<List<Ingredient>> _ingredients = ingredientRepository.findByRecipe(recipe);
            if (_ingredients.isPresent()) {
                List<Ingredient> ingredients = _ingredients.get();
                for (Ingredient i : ingredients) {
                    ingredientRepository.delete(i);
                }
            }
        }

        User user = userRepository.findByUsername(userDetails.getUsername());
        List<Recipe> myRecipes = user.getMyRecipes();
        myRecipes.remove(recipe);
        user.setMyRecipes(myRecipes);
        userRepository.save(user);
        recipeRepository.delete(recipe);
        imageRepository.deleteById(id);
//        recipeRepository.deleteById(id);
        return new ResponseEntity<>("Recipe has been deleted!",HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/my-recipes")
    public List<Recipe> getMyRecipes(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Get list of recipe that their author is user: " + userDetails.getUsername() + "...");
        Optional<List<Recipe>> potentialRecipes = recipeRepository.findByAuthor((User)userDetails);
        if (potentialRecipes.isPresent()) {
            List<Recipe> myRecipes = potentialRecipes.get();
            return myRecipes;
        }
        return new ArrayList<>();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/{id}")
    public Recipe getRecipeById(@PathVariable("id") long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Get list of recipe that their author is user: " + userDetails.getUsername() + "...");
        Optional<Recipe> potentialRecipes = recipeRepository.findById(id);
        if (potentialRecipes.isPresent()) {
            Recipe myRecipes = potentialRecipes.get();
            return myRecipes;
        }
        return new Recipe();
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/recipes/create", consumes = {"application/json"}, produces = {"application/json"})
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        if(recipe instanceof Recipe) {
            System.out.println("Create new recipe...");
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            System.out.println("Przesłany obiekt " + recipe.getTitle() + " " + recipe.getDescription() + " " + recipe.getIngredients());

            Recipe _recipe = new Recipe(recipe.getTitle(), recipe.getDescription(), (User) userDetails, recipe.getIngredients());
            recipeRepository.save(_recipe);
            System.out.println(recipe.getIngredients());

            for (Ingredient i : recipe.getIngredients()) {
                Ingredient ingredient = new Ingredient(i.getName(), i.getQuantity());
                ingredient.setRecipe(_recipe);
                ingredientRepository.save(ingredient);
            }

            User user = userRepository.findByUsername(userDetails.getUsername());
            List<Recipe> myRecipes = user.getMyRecipes();
            myRecipes.add(_recipe);
            user.setMyRecipes(myRecipes);
            userRepository.save(user);

            return _recipe;
        } else {
            return null;
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/recipes/image/{id}")
    public ImageModel getRecipeImage(@PathVariable("id") long id) {
        System.out.println("Recipe Image with ID = " + id + "...");

        final Optional<ImageModel> retrievedImage = imageRepository.findById(id);
        if(retrievedImage.isPresent()){
            ImageModel img = new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
                    decompressZLib(retrievedImage.get().getPicByte()));

            return img;
        }

        return null;
    }

    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/recipes/image/upload")
    public void uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {

        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        ImageModel img = new ImageModel(file.getOriginalFilename(), file.getContentType(),
                compressBytes(file.getBytes()));
        imageRepository.save(img);
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressZLib(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }




}
