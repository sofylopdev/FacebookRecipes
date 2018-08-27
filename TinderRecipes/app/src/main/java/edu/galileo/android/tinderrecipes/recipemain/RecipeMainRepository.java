package edu.galileo.android.tinderrecipes.recipemain;

import edu.galileo.android.tinderrecipes.entities.Recipe;

public interface RecipeMainRepository {
    public static final int COUNT = 1;//we only want one recipe
    public static final String RECENT_SORT = "r";//we want the most recent elements first
    public static final int RECIPE_RANGE = 100000;// we want an element within that range

    void getNextRecipe();

    void saveRecipe(Recipe recipe);

    void setRecipePage(int recipePage);
}
