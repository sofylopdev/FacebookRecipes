package edu.galileo.android.tinderrecipes.recipemain.ui;

import edu.galileo.android.tinderrecipes.entities.Recipe;

public interface RecipeMainView {
    void showProgress();
    void hideProgress();

    void showUIElements();
    void hidUIElements();

    void saveAnimation();
    void dismissAnimation();

    void onRecipeSaved();

    void setRecipe(Recipe recipe);
    void onGetRecipeError(String error);
}
