package edu.galileo.android.tinderrecipes.recipelist.ui;

import java.util.List;

import edu.galileo.android.tinderrecipes.entities.Recipe;

public interface RecipeListView {
    void setRecipeList(List<Recipe> data);

    void recipeUpdated();

    void recipeDeleted(Recipe recipe);
}
