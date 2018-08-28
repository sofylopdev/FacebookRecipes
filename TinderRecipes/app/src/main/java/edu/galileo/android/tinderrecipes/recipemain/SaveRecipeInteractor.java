package edu.galileo.android.tinderrecipes.recipemain;

import edu.galileo.android.tinderrecipes.entities.Recipe;

//use case: save recipe
public interface SaveRecipeInteractor {

    void execute(Recipe recipe);
}
