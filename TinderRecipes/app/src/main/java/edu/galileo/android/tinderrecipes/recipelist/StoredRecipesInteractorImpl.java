package edu.galileo.android.tinderrecipes.recipelist;

import edu.galileo.android.tinderrecipes.entities.Recipe;

public class StoredRecipesInteractorImpl implements StoredRecipesInteractor {

    private RecipeListRepository listRepository;

    public StoredRecipesInteractorImpl(RecipeListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @Override
    public void executeUpdate(Recipe recipe) {
        listRepository.updateRecipe(recipe);
    }

    @Override
    public void executeDelete(Recipe recipe) {
        listRepository.deleteRecipe(recipe);
    }
}
