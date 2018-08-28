package edu.galileo.android.tinderrecipes;

import android.app.Application;
import android.content.Intent;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.raizlabs.android.dbflow.config.FlowManager;

import edu.galileo.android.tinderrecipes.libs.di.LibsModule;
import edu.galileo.android.tinderrecipes.login.ui.LoginActivity;
import edu.galileo.android.tinderrecipes.recipemain.di.DaggerRecipeMainComponent;
import edu.galileo.android.tinderrecipes.recipemain.di.RecipeMainComponent;
import edu.galileo.android.tinderrecipes.recipemain.di.RecipeMainModule;
import edu.galileo.android.tinderrecipes.recipemain.ui.RecipeMainActivity;
import edu.galileo.android.tinderrecipes.recipemain.ui.RecipeMainView;

public class FacebookRecipesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFacebook();
        initDatabase();
    }

    private void initDatabase() {
        FlowManager.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBTearDown();
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(this);
    }

    public void logout(){
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public RecipeMainComponent getRecipeMainComponent(RecipeMainActivity activity, RecipeMainView view){
        return DaggerRecipeMainComponent
                .builder()
                .libsModule(new LibsModule(activity))
                .recipeMainModule(new RecipeMainModule(view))
                .build();
    }
}
