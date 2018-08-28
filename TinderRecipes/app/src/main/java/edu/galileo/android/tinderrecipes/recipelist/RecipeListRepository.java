package edu.galileo.android.tinderrecipes.recipelist;

import edu.galileo.android.tinderrecipes.entities.Recipe;

public interface RecipeListRepository {

    void getSavedRecipes();

    void updateRecipe(Recipe recipe);

    void deleteRecipe(Recipe recipe);

}
