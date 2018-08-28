package edu.galileo.android.tinderrecipes.recipemain;

import java.util.Random;

public class GetNextRecipeInteractorImpl implements GetNextRecipeInteractor {

    private RecipeMainRepository repository;

    public GetNextRecipeInteractorImpl(RecipeMainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        int recipePage = new Random().nextInt(RecipeMainRepository.RECIPE_RANGE);
        repository.setRecipePage(recipePage);
        repository.getNextRecipe();
    }
}
