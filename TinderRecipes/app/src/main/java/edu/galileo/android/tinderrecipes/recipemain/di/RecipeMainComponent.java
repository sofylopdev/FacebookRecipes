package edu.galileo.android.tinderrecipes.recipemain.di;

import javax.inject.Singleton;

import dagger.Component;
import edu.galileo.android.tinderrecipes.libs.base.ImageLoader;
import edu.galileo.android.tinderrecipes.libs.di.LibsModule;
import edu.galileo.android.tinderrecipes.recipemain.RecipeMainPresenter;
import edu.galileo.android.tinderrecipes.recipemain.ui.RecipeMainActivity;

@Singleton
@Component(modules = {RecipeMainModule.class, LibsModule.class})
public interface RecipeMainComponent {

   //void inject(RecipeMainActivity activity);

   ImageLoader getImageLoader();
   RecipeMainPresenter getPresenter();
}
