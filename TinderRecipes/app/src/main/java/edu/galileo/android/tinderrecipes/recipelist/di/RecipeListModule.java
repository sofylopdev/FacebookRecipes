package edu.galileo.android.tinderrecipes.recipelist.di;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.EventBus;
import edu.galileo.android.tinderrecipes.libs.base.ImageLoader;
import edu.galileo.android.tinderrecipes.recipelist.RecipeListInteractor;
import edu.galileo.android.tinderrecipes.recipelist.RecipeListInteractorImpl;
import edu.galileo.android.tinderrecipes.recipelist.RecipeListPresenter;
import edu.galileo.android.tinderrecipes.recipelist.RecipeListPresenterImpl;
import edu.galileo.android.tinderrecipes.recipelist.RecipeListRepository;
import edu.galileo.android.tinderrecipes.recipelist.RecipeListRepositoryImpl;
import edu.galileo.android.tinderrecipes.recipelist.StoredRecipesInteractor;
import edu.galileo.android.tinderrecipes.recipelist.StoredRecipesInteractorImpl;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.RecipesAdapter;

@Module
public class RecipeListModule {
    RecipeListView view;
    OnItemClickListener clickListener;

    public RecipeListModule(RecipeListView view, OnItemClickListener clickListener) {
        this.view = view;
        this.clickListener = clickListener;
    }

    @Singleton
    @Provides
    RecipeListView providesRecipeListView() {
        return this.view;
    }

    @Singleton
    @Provides
    OnItemClickListener providesOnItemClickListener() {
        return this.clickListener;
    }

    @Singleton
    @Provides
    RecipeListPresenter providesListPresenter(EventBus eventBus, RecipeListView view, RecipeListInteractor listInteractor, StoredRecipesInteractor storedInteractor) {
        return new RecipeListPresenterImpl(eventBus, view, listInteractor, storedInteractor);
    }

    @Singleton
    @Provides
    RecipeListInteractor providesRecipeListInteractor(RecipeListRepository repository) {
        return new RecipeListInteractorImpl(repository);
    }

    @Singleton
    @Provides
    StoredRecipesInteractor providesStoredRecipesInteractor(RecipeListRepository repository) {
        return new StoredRecipesInteractorImpl(repository);
    }

    @Singleton
    @Provides
    RecipeListRepository providesRecipeListRepository(EventBus eventBus) {
        return new RecipeListRepositoryImpl(eventBus);
    }


    @Singleton
    @Provides
    RecipesAdapter providesAdapter(List<Recipe> recipeList, ImageLoader imageLoader, OnItemClickListener onItemClickListener) {
        return new RecipesAdapter(recipeList, imageLoader, onItemClickListener);
    }

    @Singleton
    @Provides
    List<Recipe> providesEmptyList(){
        return new ArrayList<Recipe>();
    }
}