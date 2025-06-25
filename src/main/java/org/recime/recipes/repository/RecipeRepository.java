package org.recime.recipes.repository;

import org.recime.recipes.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    List<Recipe> findByVegetarian(boolean vegetarian);

    List<Recipe> findByServings(int servings);

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i IN :ingredients GROUP BY r HAVING COUNT(DISTINCT i) = :count")
    List<Recipe> findByIngredientsIn(@Param("ingredients") List<String> ingredients, @Param("count") long ingredientCount);

    @Query("SELECT r FROM Recipe r WHERE NOT EXISTS (SELECT i FROM r.ingredients i WHERE i IN :ingredients)")
    List<Recipe> findByIngredientsNotIn(@Param("ingredients") List<String> ingredients);

    List<Recipe> findByInstructionsContainingIgnoreCase(String instructionText);

    @Query("SELECT DISTINCT r FROM Recipe r WHERE " +
           "(:vegetarian IS NULL OR r.vegetarian = :vegetarian) AND " +
           "(:servings IS NULL OR r.servings = :servings) AND " +
           "(:instructionText IS NULL OR LOWER(r.instructions) LIKE LOWER(CONCAT('%', :instructionText, '%')))")
    List<Recipe> searchRecipes(
            @Param("vegetarian") Boolean vegetarian,
            @Param("servings") Integer servings,
            @Param("instructionText") String instructionText);
}