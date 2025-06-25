package org.recime.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {
    private UUID id;
    private String title;
    private String description;
    private List<String> ingredients = new ArrayList<>();
    private String instructions;
    private boolean vegetarian;
    private int servings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}