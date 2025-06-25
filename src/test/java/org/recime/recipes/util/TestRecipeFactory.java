package org.recime.recipes.util;

import org.recime.recipes.dto.RecipeDto;
import org.recime.recipes.dto.RecipeRequest;
import org.recime.recipes.model.Recipe;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Utility class for creating test recipe data.
 * Provides methods to generate generic recipe objects for testing purposes.
 */
public class TestRecipeFactory {

    /**
     * Creates a basic recipe DTO with default values.
     * 
     * @param id The UUID for the recipe (can be null for new recipes)
     * @return A basic RecipeDto instance
     */
    public static RecipeDto createBasicRecipeDto(UUID id) {
        return RecipeDto.builder()
                .id(id != null ? id : UUID.randomUUID())
                .title("Basic Test Recipe")
                .description("A simple test recipe description")
                .ingredients(Arrays.asList("Ingredient 1", "Ingredient 2", "Ingredient 3"))
                .instructions("Mix all ingredients and cook")
                .vegetarian(false)
                .servings(4)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a vegetarian recipe DTO.
     * 
     * @param id The UUID for the recipe (can be null for new recipes)
     * @return A vegetarian RecipeDto instance
     */
    public static RecipeDto createVegetarianRecipeDto(UUID id) {
        return RecipeDto.builder()
                .id(id != null ? id : UUID.randomUUID())
                .title("Vegetarian Test Recipe")
                .description("A vegetarian test recipe description")
                .ingredients(Arrays.asList("Vegetable 1", "Vegetable 2", "Spice 1"))
                .instructions("Prepare vegetables and cook with spices")
                .vegetarian(true)
                .servings(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a custom recipe DTO with specified properties.
     * 
     * @param id The UUID for the recipe (can be null for new recipes)
     * @param title Recipe title
     * @param description Recipe description
     * @param ingredients List of ingredients
     * @param instructions Cooking instructions
     * @param vegetarian Whether the recipe is vegetarian
     * @param servings Number of servings
     * @return A customized RecipeDto instance
     */
    public static RecipeDto createCustomRecipeDto(
            UUID id, 
            String title, 
            String description, 
            List<String> ingredients, 
            String instructions, 
            boolean vegetarian, 
            int servings) {
        
        return RecipeDto.builder()
                .id(id != null ? id : UUID.randomUUID())
                .title(title)
                .description(description)
                .ingredients(ingredients)
                .instructions(instructions)
                .vegetarian(vegetarian)
                .servings(servings)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a basic recipe request with default values.
     * 
     * @return A basic RecipeRequest instance
     */
    public static RecipeRequest createBasicRecipeRequest() {
        return RecipeRequest.builder()
                .title("Basic Test Recipe")
                .description("A simple test recipe description")
                .ingredients(Arrays.asList("Ingredient 1", "Ingredient 2", "Ingredient 3"))
                .instructions("Mix all ingredients and cook")
                .vegetarian(false)
                .servings(4)
                .build();
    }

    /**
     * Creates a vegetarian recipe request.
     * 
     * @return A vegetarian RecipeRequest instance
     */
    public static RecipeRequest createVegetarianRecipeRequest() {
        return RecipeRequest.builder()
                .title("Vegetarian Test Recipe")
                .description("A vegetarian test recipe description")
                .ingredients(Arrays.asList("Vegetable 1", "Vegetable 2", "Spice 1"))
                .instructions("Prepare vegetables and cook with spices")
                .vegetarian(true)
                .servings(2)
                .build();
    }

    /**
     * Creates a custom recipe request with specified properties.
     * 
     * @param title Recipe title
     * @param description Recipe description
     * @param ingredients List of ingredients
     * @param instructions Cooking instructions
     * @param vegetarian Whether the recipe is vegetarian
     * @param servings Number of servings
     * @return A customized RecipeRequest instance
     */
    public static RecipeRequest createCustomRecipeRequest(
            String title, 
            String description, 
            List<String> ingredients, 
            String instructions, 
            boolean vegetarian, 
            int servings) {
        
        return RecipeRequest.builder()
                .title(title)
                .description(description)
                .ingredients(ingredients)
                .instructions(instructions)
                .vegetarian(vegetarian)
                .servings(servings)
                .build();
    }

    /**
     * Creates a basic Recipe entity with default values.
     * 
     * @param id The UUID for the recipe (can be null for new recipes)
     * @return A basic Recipe entity instance
     */
    public static Recipe createBasicRecipe(UUID id) {
        return Recipe.builder()
                .id(id != null ? id : UUID.randomUUID())
                .title("Basic Test Recipe")
                .description("A simple test recipe description")
                .ingredients(Arrays.asList("Ingredient 1", "Ingredient 2", "Ingredient 3"))
                .instructions("Mix all ingredients and cook")
                .vegetarian(false)
                .servings(4)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a vegetarian Recipe entity.
     * 
     * @param id The UUID for the recipe (can be null for new recipes)
     * @return A vegetarian Recipe entity instance
     */
    public static Recipe createVegetarianRecipe(UUID id) {
        return Recipe.builder()
                .id(id != null ? id : UUID.randomUUID())
                .title("Vegetarian Test Recipe")
                .description("A vegetarian test recipe description")
                .ingredients(Arrays.asList("Vegetable 1", "Vegetable 2", "Spice 1"))
                .instructions("Prepare vegetables and cook with spices")
                .vegetarian(true)
                .servings(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a custom Recipe entity with specified properties.
     * 
     * @param id The UUID for the recipe (can be null for new recipes)
     * @param title Recipe title
     * @param description Recipe description
     * @param ingredients List of ingredients
     * @param instructions Cooking instructions
     * @param vegetarian Whether the recipe is vegetarian
     * @param servings Number of servings
     * @return A customized Recipe entity instance
     */
    public static Recipe createCustomRecipe(
            UUID id, 
            String title, 
            String description, 
            List<String> ingredients, 
            String instructions, 
            boolean vegetarian, 
            int servings) {
        
        return Recipe.builder()
                .id(id != null ? id : UUID.randomUUID())
                .title(title)
                .description(description)
                .ingredients(ingredients)
                .instructions(instructions)
                .vegetarian(vegetarian)
                .servings(servings)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}