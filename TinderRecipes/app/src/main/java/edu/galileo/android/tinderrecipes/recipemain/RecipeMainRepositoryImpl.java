package edu.galileo.android.tinderrecipes.recipemain;

import android.util.Log;

import java.util.Random;

import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.api.RecipeSearchResponse;
import edu.galileo.android.tinderrecipes.api.RecipesService;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.EventBus;
import edu.galileo.android.tinderrecipes.recipemain.events.RecipeMainEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeMainRepositoryImpl implements RecipeMainRepository {

    private int recipePage;
    private EventBus eventBus;
    private RecipesService service;

    public RecipeMainRepositoryImpl(EventBus eventBus, RecipesService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getNextRecipe() {
        Call<RecipeSearchResponse> call = service.search(BuildConfig.FOOD_API_KEY,
                RECENT_SORT, COUNT, recipePage);
        Log.d("Repository", service.search(BuildConfig.FOOD_API_KEY,
                RECENT_SORT, COUNT, recipePage).toString());
        call.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                if (response.isSuccessful()) {
                    RecipeSearchResponse recipeSearchResponse = response.body();
                    Log.d("Repository", "Got a successfull response");
                    if (recipeSearchResponse.getCount() == 0) {  //the number that i sent as a page is invalid
                        Log.d("Repository", "Count is zero");
                        setRecipePage(new Random(RECIPE_RANGE).nextInt());
                        getNextRecipe();
                    } else {
                        Log.d("Repository", "Count NOT zero");
                        Recipe recipe = recipeSearchResponse.getFirstRecipe();
                        if (recipe != null) {
                            Log.d("Repository", "Posting the recipe");
                            post(recipe);
                        }else{
                            Log.d("Repository", "Posting the error");
                           post(response.message());
                        }
                    }
                } else {
                    Log.d("Repository", "Got UNsuccessfull response");
                    post(response.message());
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                post(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        recipe.save();
        post();
    }

    @Override
    public void setRecipePage(int recipePage) {
        Log.d("Repository", "next page: "+ recipePage);
        this.recipePage = recipePage;
    }

    private void post(Recipe recipe) {
        post(null, RecipeMainEvent.NEXT_EVENT, recipe);
    }

    private void post(String error) {
        post(error, RecipeMainEvent.NEXT_EVENT, null);
    }

    private void post() {
        post(null, RecipeMainEvent.SAVE_EVENT, null);
    }

    private void post(String error, int type, Recipe recipe) {
        RecipeMainEvent event = new RecipeMainEvent();
        event.setType(type);
        event.setRecipe(recipe);
        event.setError(error);
        eventBus.post(event);
    }
}
