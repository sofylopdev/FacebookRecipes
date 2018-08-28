package edu.galileo.android.tinderrecipes.recipelist;

import edu.galileo.android.tinderrecipes.entities.Recipe;

//related to the stored data
public interface StoredRecipesInteractor {
    void executeUpdate(Recipe recipe);//action: store the recipe
    void executeDelete(Recipe recipe);//action: delete
}
