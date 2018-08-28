package edu.galileo.android.tinderrecipes.recipelist;

import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.recipelist.events.RecipeListEvent;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListView;


public interface RecipeListPresenter {

    void onCreate();
    void onDestroy();
    void onEventMainThread(RecipeListEvent event);

    void getRecipes();
    void removeRecipe(Recipe recipe);
    void toggleFavorite(Recipe recipe);

    RecipeListView getView();
}
