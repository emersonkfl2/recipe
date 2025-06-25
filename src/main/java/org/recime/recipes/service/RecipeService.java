package org.recime.recipes.service;

import lombok.RequiredArgsConstructor;
import org.recime.recipes.dto.RecipeDto;
import org.recime.recipes.dto.RecipeRequest;
import org.recime.recipes.dto.RecipeSearchRequest;
import org.recime.recipes.exception.RecipeNotFoundException;
import org.recime.recipes.model.Recipe;
import org.recime.recipes.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeConverter recipeConverter;

    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipeConverter::mapToDto)
                .collect(Collectors.toList());
    }

    public RecipeDto getRecipeById(UUID id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        return recipeConverter.mapToDto(recipe);
    }

    public RecipeDto createRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = recipeConverter.mapRequestToRecipe(recipeRequest);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return recipeConverter.mapToDto(savedRecipe);
    }

    public RecipeDto updateRecipe(UUID id, RecipeRequest recipeRequest) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));

        recipeConverter.updateRecipeFromRequest(existingRecipe, recipeRequest);

        Recipe updatedRecipe = recipeRepository.save(existingRecipe);
        return recipeConverter.mapToDto(updatedRecipe);
    }

    public void deleteRecipe(UUID id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(id);
        }
        recipeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> searchRecipes(RecipeSearchRequest searchRequest) {
        List<Recipe> results;

        if (!searchRequest.getIncludeIngredients().isEmpty() || !searchRequest.getExcludeIngredients().isEmpty()) {
            results = searchRecipesWithIngredients(searchRequest);
        } else {
            results = recipeRepository.searchRecipes(
                    searchRequest.getVegetarian(),
                    searchRequest.getServings(),
                    searchRequest.getInstructionText()
            );
        }

        return results.stream()
                .map(recipeConverter::mapToDto)
                .collect(Collectors.toList());
    }

    private List<Recipe> searchRecipesWithIngredients(RecipeSearchRequest searchRequest) {
        List<Recipe> results = recipeRepository.searchRecipes(
                searchRequest.getVegetarian(),
                searchRequest.getServings(),
                searchRequest.getInstructionText()
        );

        if (!searchRequest.getIncludeIngredients().isEmpty()) {
            results = filterByIncludedIngredients(results, searchRequest.getIncludeIngredients());
        }

        if (!searchRequest.getExcludeIngredients().isEmpty()) {
            results = filterByExcludedIngredients(results, searchRequest.getExcludeIngredients());
        }

        return results;
    }

    private List<Recipe> filterByIncludedIngredients(List<Recipe> recipes, List<String> includeIngredients) {
        return recipes.stream()
                .filter(recipe -> new HashSet<>(recipe.getIngredients()).containsAll(includeIngredients))
                .collect(Collectors.toList());
    }

    private List<Recipe> filterByExcludedIngredients(List<Recipe> recipes, List<String> excludeIngredients) {
        return recipes.stream()
                .filter(recipe -> recipe.getIngredients().stream()
                        .noneMatch(excludeIngredients::contains))
                .collect(Collectors.toList());
    }

}
