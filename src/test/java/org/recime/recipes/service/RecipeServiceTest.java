package org.recime.recipes.service;

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
import org.recime.recipes.model.Recipe;
import org.recime.recipes.repository.RecipeRepository;
import org.recime.recipes.util.TestRecipeFactory;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock(lenient = true)
    private RecipeConverter recipeConverter;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe testRecipe;
    private RecipeRequest testRecipeRequest;
    private RecipeDto testRecipeDto;
    private LocalDateTime now;
    private UUID testUuid;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        testUuid = UUID.randomUUID();

        // Create test data using the TestRecipeFactory
        testRecipe = TestRecipeFactory.createCustomRecipe(
                testUuid,
                "Test Recipe",
                "Test Description",
                Arrays.asList("Ingredient 1", "Ingredient 2"),
                "Test Instructions",
                true,
                4
        );

        // Set timestamps to control test values
        testRecipe.setCreatedAt(now);
        testRecipe.setUpdatedAt(now);

        testRecipeRequest = TestRecipeFactory.createCustomRecipeRequest(
                "Test Recipe",
                "Test Description",
                Arrays.asList("Ingredient 1", "Ingredient 2"),
                "Test Instructions",
                true,
                4
        );

        testRecipeDto = TestRecipeFactory.createCustomRecipeDto(
                testUuid,
                "Test Recipe",
                "Test Description",
                Arrays.asList("Ingredient 1", "Ingredient 2"),
                "Test Instructions",
                true,
                4
        );

        // Set timestamps to control test values
        testRecipeDto.setCreatedAt(now);
        testRecipeDto.setUpdatedAt(now);

        // Set up RecipeConverter mock behavior
        when(recipeConverter.mapToDto(any(Recipe.class))).thenReturn(testRecipeDto);
        when(recipeConverter.mapRequestToRecipe(any(RecipeRequest.class))).thenReturn(testRecipe);
        doNothing().when(recipeConverter).updateRecipeFromRequest(any(Recipe.class), any(RecipeRequest.class));
    }

    @Test
    void getAllRecipes_ShouldReturnAllRecipes() {
        when(recipeRepository.findAll()).thenReturn(Arrays.asList(testRecipe));

        List<RecipeDto> result = recipeService.getAllRecipes();

        assertEquals(1, result.size());
        assertEquals(testRecipe.getId(), result.get(0).getId());
        assertEquals(testRecipe.getTitle(), result.get(0).getTitle());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getRecipeById_WithValidId_ShouldReturnRecipe() {
        when(recipeRepository.findById(testUuid)).thenReturn(Optional.of(testRecipe));

        RecipeDto result = recipeService.getRecipeById(testUuid);

        assertNotNull(result);
        assertEquals(testRecipe.getId(), result.getId());
        assertEquals(testRecipe.getTitle(), result.getTitle());
        verify(recipeRepository, times(1)).findById(testUuid);
    }

    @Test
    void getRecipeById_WithInvalidId_ShouldThrowException() {
        UUID invalidId = UUID.randomUUID();
        when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(invalidId));
        verify(recipeRepository, times(1)).findById(invalidId);
    }

    @Test
    void createRecipe_ShouldSaveAndReturnRecipe() {
        Recipe unsavedRecipe = TestRecipeFactory.createCustomRecipe(
                null, // No ID for unsaved recipe
                testRecipeRequest.getTitle(),
                testRecipeRequest.getDescription(),
                testRecipeRequest.getIngredients(),
                testRecipeRequest.getInstructions(),
                testRecipeRequest.isVegetarian(),
                testRecipeRequest.getServings()
        );

        when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

        RecipeDto result = recipeService.createRecipe(testRecipeRequest);

        assertNotNull(result);
        assertEquals(testRecipe.getId(), result.getId());
        assertEquals(testRecipe.getTitle(), result.getTitle());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    void updateRecipe_WithValidId_ShouldUpdateAndReturnRecipe() {
        when(recipeRepository.findById(testUuid)).thenReturn(Optional.of(testRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

        RecipeDto result = recipeService.updateRecipe(testUuid, testRecipeRequest);

        assertNotNull(result);
        assertEquals(testRecipe.getId(), result.getId());
        assertEquals(testRecipe.getTitle(), result.getTitle());
        verify(recipeRepository, times(1)).findById(testUuid);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    void updateRecipe_WithInvalidId_ShouldThrowException() {
        UUID invalidId = UUID.randomUUID();
        when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(invalidId, testRecipeRequest));
        verify(recipeRepository, times(1)).findById(invalidId);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void deleteRecipe_WithValidId_ShouldDeleteRecipe() {
        UUID deleteId = UUID.randomUUID();
        when(recipeRepository.existsById(deleteId)).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(deleteId);

        recipeService.deleteRecipe(deleteId);

        verify(recipeRepository, times(1)).existsById(deleteId);
        verify(recipeRepository, times(1)).deleteById(deleteId);
    }

    @Test
    void deleteRecipe_WithInvalidId_ShouldThrowException() {
        UUID invalidId = UUID.randomUUID();
        when(recipeRepository.existsById(invalidId)).thenReturn(false);

        assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteRecipe(invalidId));
        verify(recipeRepository, times(1)).existsById(invalidId);
        verify(recipeRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void searchRecipes_WithNoFilters_ShouldReturnAllMatchingRecipes() {
        RecipeSearchRequest searchRequest = new RecipeSearchRequest();
        when(recipeRepository.searchRecipes(null, null, null)).thenReturn(Arrays.asList(testRecipe));

        List<RecipeDto> result = recipeService.searchRecipes(searchRequest);

        assertEquals(1, result.size());
        assertEquals(testRecipe.getId(), result.get(0).getId());
        verify(recipeRepository, times(1)).searchRecipes(null, null, null);
    }

    @Test
    void searchRecipes_WithVegetarianFilter_ShouldReturnMatchingRecipes() {
        RecipeSearchRequest searchRequest = RecipeSearchRequest.builder()
                .vegetarian(true)
                .includeIngredients(new ArrayList<>())
                .excludeIngredients(new ArrayList<>())
                .build();

        when(recipeRepository.searchRecipes(true, null, null)).thenReturn(Arrays.asList(testRecipe));

        List<RecipeDto> result = recipeService.searchRecipes(searchRequest);

        assertEquals(1, result.size());
        assertEquals(testRecipe.getId(), result.get(0).getId());
        assertTrue(result.get(0).isVegetarian());
        verify(recipeRepository, times(1)).searchRecipes(true, null, null);
    }

    @Test
    void searchRecipes_WithIncludeIngredients_ShouldFilterResults() {
        RecipeSearchRequest searchRequest = RecipeSearchRequest.builder()
                .includeIngredients(Arrays.asList("Ingredient 1"))
                .excludeIngredients(new ArrayList<>())
                .build();

        when(recipeRepository.searchRecipes(null, null, null)).thenReturn(Arrays.asList(testRecipe));

        List<RecipeDto> result = recipeService.searchRecipes(searchRequest);

        assertEquals(1, result.size());
        assertEquals(testRecipe.getId(), result.get(0).getId());
        verify(recipeRepository, times(1)).searchRecipes(null, null, null);
    }

    @Test
    void searchRecipes_WithExcludeIngredients_ShouldFilterResults() {
        RecipeSearchRequest searchRequest = RecipeSearchRequest.builder()
                .excludeIngredients(Arrays.asList("Ingredient 3"))
                .includeIngredients(new ArrayList<>())
                .build();

        when(recipeRepository.searchRecipes(null, null, null)).thenReturn(Arrays.asList(testRecipe));

        List<RecipeDto> result = recipeService.searchRecipes(searchRequest);

        assertEquals(1, result.size());
        assertEquals(testRecipe.getId(), result.get(0).getId());
        verify(recipeRepository, times(1)).searchRecipes(null, null, null);
    }

    @Test
    void searchRecipes_WithNoMatchingResults_ShouldReturnEmptyList() {
        RecipeSearchRequest searchRequest = RecipeSearchRequest.builder()
                .vegetarian(false)
                .includeIngredients(new ArrayList<>())
                .excludeIngredients(new ArrayList<>())
                .build();

        when(recipeRepository.searchRecipes(false, null, null)).thenReturn(Collections.emptyList());

        List<RecipeDto> result = recipeService.searchRecipes(searchRequest);

        assertTrue(result.isEmpty());
        verify(recipeRepository, times(1)).searchRecipes(false, null, null);
    }
}
