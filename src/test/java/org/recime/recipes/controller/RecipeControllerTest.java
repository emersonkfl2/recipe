package org.recime.recipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recime.recipes.dto.RecipeDto;
import org.recime.recipes.dto.RecipeRequest;
import org.recime.recipes.dto.RecipeSearchRequest;
import org.recime.recipes.exception.RecipeNotFoundException;
import org.recime.recipes.service.RecipeService;
import org.recime.recipes.util.TestRecipeFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void testGetAllRecipes() throws Exception {
        RecipeDto recipe1 = TestRecipeFactory.createCustomRecipeDto(
                UUID.randomUUID(),
                "Pasta Carbonara",
                "Classic Italian pasta dish",
                Arrays.asList("Pasta", "Eggs", "Cheese", "Bacon"),
                "Cook pasta, mix with eggs and cheese, add bacon",
                false,
                2
        );

        RecipeDto recipe2 = TestRecipeFactory.createCustomRecipeDto(
                UUID.randomUUID(),
                "Vegetable Stir Fry",
                "Quick and healthy vegetable dish",
                Arrays.asList("Broccoli", "Carrots", "Peppers", "Soy Sauce"),
                "Stir fry vegetables, add sauce",
                true,
                4
        );

        List<RecipeDto> recipes = Arrays.asList(recipe1, recipe2);

        when(recipeService.getAllRecipes()).thenReturn(recipes);

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Pasta Carbonara")))
                .andExpect(jsonPath("$[1].title", is("Vegetable Stir Fry")));
    }

    @Test
    public void testCreateRecipe() throws Exception {
        RecipeRequest request = TestRecipeFactory.createCustomRecipeRequest(
                "Chocolate Cake",
                "Delicious chocolate cake",
                Arrays.asList("Flour", "Sugar", "Cocoa", "Eggs"),
                "Mix dry ingredients, add wet ingredients, bake",
                true,
                8
        );

        RecipeDto response = TestRecipeFactory.createCustomRecipeDto(
                UUID.randomUUID(),
                request.getTitle(),
                request.getDescription(),
                request.getIngredients(),
                request.getInstructions(),
                request.isVegetarian(),
                request.getServings()
        );

        when(recipeService.createRecipe(any(RecipeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title", is("Chocolate Cake")));
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        RecipeRequest request = TestRecipeFactory.createCustomRecipeRequest(
                "Updated Pasta Recipe",
                "Updated description",
                Arrays.asList("Pasta", "Sauce"),
                "Updated instructions",
                true,
                3
        );

        RecipeDto response = TestRecipeFactory.createCustomRecipeDto(
                UUID.randomUUID(),
                request.getTitle(),
                request.getDescription(),
                request.getIngredients(),
                request.getInstructions(),
                request.isVegetarian(),
                request.getServings()
        );

        UUID testId = UUID.randomUUID();
        when(recipeService.updateRecipe(eq(testId), any(RecipeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/recipes/" + testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title", is("Updated Pasta Recipe")));
    }

    @Test
    public void testGetRecipeById() throws Exception {
        RecipeDto recipe = TestRecipeFactory.createCustomRecipeDto(
                UUID.randomUUID(),
                "Pasta Carbonara",
                "Classic Italian pasta dish",
                Arrays.asList("Pasta", "Eggs", "Cheese", "Bacon"),
                "Cook pasta, mix with eggs and cheese, add bacon",
                false,
                2
        );

        UUID testId = UUID.randomUUID();
        when(recipeService.getRecipeById(testId)).thenReturn(recipe);

        mockMvc.perform(get("/api/recipes/" + testId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title", is("Pasta Carbonara")))
                .andExpect(jsonPath("$.ingredients", hasSize(4)))
                .andExpect(jsonPath("$.vegetarian", is(false)));
    }

    @Test
    public void testGetRecipeById_NotFound() throws Exception {
        UUID testId = UUID.randomUUID();
        when(recipeService.getRecipeById(testId)).thenThrow(new RecipeNotFoundException(testId));

        mockMvc.perform(get("/api/recipes/" + testId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        UUID testId = UUID.randomUUID();
        doNothing().when(recipeService).deleteRecipe(testId);

        mockMvc.perform(delete("/api/recipes/" + testId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteRecipe_NotFound() throws Exception {
        UUID testId = UUID.randomUUID();
        doThrow(new RecipeNotFoundException(testId)).when(recipeService).deleteRecipe(testId);

        mockMvc.perform(delete("/api/recipes/" + testId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchRecipes() throws Exception {
        RecipeSearchRequest searchRequest = RecipeSearchRequest.builder()
                .vegetarian(true)
                .servings(4)
                .includeIngredients(Arrays.asList("Tomato", "Basil"))
                .excludeIngredients(Arrays.asList("Meat"))
                .instructionText("bake")
                .build();

        RecipeDto recipe = TestRecipeFactory.createCustomRecipeDto(
                UUID.randomUUID(),
                "Vegetable Lasagna",
                "Delicious vegetarian lasagna",
                Arrays.asList("Pasta", "Tomato", "Basil", "Cheese"),
                "Layer ingredients and bake for 30 minutes",
                true,
                4
        );

        when(recipeService.searchRecipes(any(RecipeSearchRequest.class))).thenReturn(Arrays.asList(recipe));

        mockMvc.perform(post("/api/recipes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title", is("Vegetable Lasagna")))
                .andExpect(jsonPath("$[0].vegetarian", is(true)))
                .andExpect(jsonPath("$[0].servings", is(4)));
    }

    @Test
    public void testSearchRecipes_NoResults() throws Exception {
        RecipeSearchRequest searchRequest = RecipeSearchRequest.builder()
                .vegetarian(true)
                .servings(10)
                .includeIngredients(new ArrayList<>())
                .excludeIngredients(new ArrayList<>())
                .build();

        when(recipeService.searchRecipes(any(RecipeSearchRequest.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/api/recipes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
