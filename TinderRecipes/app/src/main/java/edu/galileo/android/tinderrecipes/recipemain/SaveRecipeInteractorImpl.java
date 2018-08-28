package edu.galileo.android.tinderrecipes.recipemain;

import edu.galileo.android.tinderrecipes.entities.Recipe;

public class SaveRecipeInteractorImpl implements SaveRecipeInteractor {

    private RecipeMainRepository repository;

    public SaveRecipeInteractorImpl(RecipeMainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Recipe recipe) {
        repository.saveRecipe(recipe);
    }
}
