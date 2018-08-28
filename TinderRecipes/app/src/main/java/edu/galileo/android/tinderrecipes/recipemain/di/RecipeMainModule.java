package edu.galileo.android.tinderrecipes.recipemain.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.galileo.android.tinderrecipes.api.RecipesClient;
import edu.galileo.android.tinderrecipes.api.RecipesService;
import edu.galileo.android.tinderrecipes.libs.base.EventBus;
import edu.galileo.android.tinderrecipes.recipemain.GetNextRecipeInteractor;
import edu.galileo.android.tinderrecipes.recipemain.GetNextRecipeInteractorImpl;
import edu.galileo.android.tinderrecipes.recipemain.RecipeMainPresenter;
import edu.galileo.android.tinderrecipes.recipemain.RecipeMainPresenterImpl;
import edu.galileo.android.tinderrecipes.recipemain.RecipeMainRepository;
import edu.galileo.android.tinderrecipes.recipemain.RecipeMainRepositoryImpl;
import edu.galileo.android.tinderrecipes.recipemain.SaveRecipeInteractor;
import edu.galileo.android.tinderrecipes.recipemain.SaveRecipeInteractorImpl;
import edu.galileo.android.tinderrecipes.recipemain.ui.RecipeMainView;

@Module
public class RecipeMainModule {

    private RecipeMainView view;

    public RecipeMainModule(RecipeMainView view) {
        this.view = view;
    }

    @Provides @Singleton
    RecipeMainView providesRecipeMainView(){
        return this.view;
    }

    @Provides @Singleton
    RecipeMainPresenter providesRecipeMainPresenter(EventBus eventBus, RecipeMainView view, SaveRecipeInteractor saveInteractor, GetNextRecipeInteractor getNextInteractor){
        return new RecipeMainPresenterImpl(eventBus, view, saveInteractor, getNextInteractor);
    }

    @Provides @Singleton
    SaveRecipeInteractor providesSaveRecipeInteractor(RecipeMainRepository repository){
        return new SaveRecipeInteractorImpl(repository);
    }

    @Provides @Singleton
    GetNextRecipeInteractor providesGetNextRecipeInteractor(RecipeMainRepository repository){
        return new GetNextRecipeInteractorImpl(repository);
    }

    @Provides @Singleton
    RecipeMainRepository providesRecipeMainRepository(EventBus eventBus, RecipesService service){
        return new RecipeMainRepositoryImpl(eventBus, service);
    }

    //If this was needed in other activities this would be better in the LibsModule
    @Provides @Singleton
    RecipesService providesRecipeService(){
        return new RecipesClient().getRecipesService();
    }
}
