package edu.galileo.android.tinderrecipes.recipelist.events;

import java.util.List;

import edu.galileo.android.tinderrecipes.entities.Recipe;

public class RecipeListEvent {
    private int type;
    private List<Recipe> recipeList;

    public static final int READ_EVENT = 0;
    public static final int UPDATE_EVENT = 1;
    public static final int DELETE_EVENT = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
}
