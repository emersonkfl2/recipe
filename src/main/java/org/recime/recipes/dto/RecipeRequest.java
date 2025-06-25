package org.recime.recipes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class RecipeRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotEmpty(message = "At least one ingredient is required")
    private List<String> ingredients = new ArrayList<>();
    
    @NotBlank(message = "Instructions are required")
    private String instructions;
    
    private boolean vegetarian;
    
    @Min(value = 1, message = "Servings must be at least 1")
    private int servings;
}