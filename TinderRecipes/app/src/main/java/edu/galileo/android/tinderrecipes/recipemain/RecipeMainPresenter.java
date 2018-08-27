package edu.galileo.android.tinderrecipes.recipemain;

import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.recipemain.events.RecipeMainEvent;
import edu.galileo.android.tinderrecipes.recipemain.ui.RecipeMainView;

public interface RecipeMainPresenter {

    void onCreate();
    void onDestroy();

    void dismissRecipe();
    void getNextRecipe();
    void saveRecipe(Recipe recipe);
    void onEventMainThread(RecipeMainEvent event);

    //used in testing
    RecipeMainView getView();
}
