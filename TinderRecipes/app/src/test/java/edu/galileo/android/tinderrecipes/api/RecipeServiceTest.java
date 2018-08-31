package edu.galileo.android.tinderrecipes.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Random;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.recipemain.RecipeMainRepository;
import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class RecipeServiceTest extends BaseTest {
    private RecipesService service;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        RecipesClient client = new RecipesClient();
        service = client.getRecipesService();
    }

    @Test//we are testing the result
    public void doSearch_getRecipeFromBackend() throws Exception {
        String sort = RecipeMainRepository.RECENT_SORT;
        int count = RecipeMainRepository.COUNT;
        int page = 1;//i know the api has this value

        Call<RecipeSearchResponse> call = service.search(BuildConfig.FOOD_API_KEY, sort, count, page);

        Response<RecipeSearchResponse> response = call.execute();//synchronous call
        assertTrue(response.isSuccessful());

        RecipeSearchResponse recipeSearchResponse = response.body();
        assertEquals(1, recipeSearchResponse.getCount());// checking if the count is equal to one, if the body has content

        Recipe recipe = recipeSearchResponse.getFirstRecipe();
        assertNotNull(recipe);
    }

    @Test
    public void doSearch_getNoRecipeFromBackend() throws Exception {
        String sort = RecipeMainRepository.RECENT_SORT;
        int count = RecipeMainRepository.COUNT;
        int page = 1000000000;

        Call<RecipeSearchResponse> call = service.search(BuildConfig.FOOD_API_KEY, sort, count, page);

        Response<RecipeSearchResponse> response = call.execute();//synchronous call
        assertTrue(response.isSuccessful());

        RecipeSearchResponse recipeSearchResponse = response.body();
        assertEquals(0, recipeSearchResponse.getCount());// checking if the count is equal to zero, if the body has content

        Recipe recipe = recipeSearchResponse.getFirstRecipe();
        assertNull(recipe);
    }

    @Test//this one is similar to the app
    public void doSearch_getRandomRecipeFromBackend() throws Exception {
        String sort = RecipeMainRepository.RECENT_SORT;
        int count = RecipeMainRepository.COUNT;
        int page = new Random().nextInt(RecipeMainRepository.RECIPE_RANGE);

        Call<RecipeSearchResponse> call = service.search(BuildConfig.FOOD_API_KEY, sort, count, page);

        Response<RecipeSearchResponse> response = call.execute();//synchronous call
        assertTrue(response.isSuccessful());

        RecipeSearchResponse recipeSearchResponse = response.body();
        if (recipeSearchResponse.getCount() == 1) {
            Recipe recipe = recipeSearchResponse.getFirstRecipe();
            assertNotNull(recipe);
        } else {
            //In case the random number is not valid
            System.out.println("Invalid recipe, try again");
        }
    }
}
