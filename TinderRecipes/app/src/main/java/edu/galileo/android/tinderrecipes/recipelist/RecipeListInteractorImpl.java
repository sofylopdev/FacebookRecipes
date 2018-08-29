package edu.galileo.android.tinderrecipes.recipelist;

public class RecipeListInteractorImpl implements RecipeListInteractor{

    private RecipeListRepository listRepository;

    public RecipeListInteractorImpl(RecipeListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @Override
    public void execute() {
        listRepository.getSavedRecipes();
    }
}
