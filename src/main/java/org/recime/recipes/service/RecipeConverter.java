package org.recime.recipes.service;

import org.recime.recipes.dto.RecipeDto;
import org.recime.recipes.dto.RecipeRequest;
import org.recime.recipes.model.Recipe;
import org.springframework.stereotype.Component;

@Component
public class RecipeConverter {

    public RecipeDto mapToDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .ingredients(recipe.getIngredients())
                .instructions(recipe.getInstructions())
                .vegetarian(recipe.isVegetarian())
                .servings(recipe.getServings())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .build();
    }

    public Recipe mapRequestToRecipe(RecipeRequest recipeRequest) {
        return Recipe.builder()
                .title(recipeRequest.getTitle())
                .description(recipeRequest.getDescription())
                .ingredients(recipeRequest.getIngredients())
                .instructions(recipeRequest.getInstructions())
                .vegetarian(recipeRequest.isVegetarian())
                .servings(recipeRequest.getServings())
                .build();
    }

    public void updateRecipeFromRequest(Recipe recipe, RecipeRequest recipeRequest) {
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setIngredients(recipeRequest.getIngredients());
        recipe.setInstructions(recipeRequest.getInstructions());
        recipe.setVegetarian(recipeRequest.isVegetarian());
        recipe.setServings(recipeRequest.getServings());
    }
}