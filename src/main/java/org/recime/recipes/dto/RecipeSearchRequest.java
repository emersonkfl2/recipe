package org.recime.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeSearchRequest {
    private Boolean vegetarian;
    private Integer servings;
    private List<String> includeIngredients = new ArrayList<>();
    private List<String> excludeIngredients = new ArrayList<>();
    private String instructionText;
}