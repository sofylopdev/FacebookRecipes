package edu.galileo.android.tinderrecipes.recipelist.di;

import javax.inject.Singleton;

import dagger.Component;
import edu.galileo.android.tinderrecipes.libs.di.LibsModule;
import edu.galileo.android.tinderrecipes.recipelist.RecipeListPresenter;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.RecipesAdapter;

@Singleton
@Component(modules = {RecipeListModule.class, LibsModule.class})
public interface RecipeListComponent {

    RecipesAdapter getAdapter();

    RecipeListPresenter getPresenter();

}
