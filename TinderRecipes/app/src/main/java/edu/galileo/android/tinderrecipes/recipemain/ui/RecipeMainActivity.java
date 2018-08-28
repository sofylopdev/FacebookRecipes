package edu.galileo.android.tinderrecipes.recipemain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.tinderrecipes.FacebookRecipesApp;
import edu.galileo.android.tinderrecipes.R;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.ImageLoader;
import edu.galileo.android.tinderrecipes.recipemain.RecipeMainPresenter;
import edu.galileo.android.tinderrecipes.recipemain.di.RecipeMainComponent;

public class RecipeMainActivity extends AppCompatActivity implements RecipeMainView, SwipeGestureListener {

    @BindView(R.id.imgRecipe)
    ImageView imgRecipe;
    @BindView(R.id.imgDismiss)
    ImageButton imgDismiss;
    @BindView(R.id.imgKeep)
    ImageButton imgKeep;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layoutContainer)
    ConstraintLayout layoutContainer;

    private RecipeMainPresenter presenter;
    private Recipe currentRecipe;
    private ImageLoader imageLoader;
    private RecipeMainComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);
        ButterKnife.bind(this);
        setupInjection();
        setupImageLoader();
        setupGestureDetector();
        presenter.onCreate();
        presenter.getNextRecipe();
    }

    private void setupGestureDetector() {
        final GestureDetector gestureDetector = new GestureDetector(this, new SwipeGestureDetector(this));
        View.OnTouchListener gestureOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //This way we are connecting the events
                return gestureDetector.onTouchEvent(event);
            }
        };
        imgRecipe.setOnTouchListener(gestureOnTouchListener);
    }

    private void setupImageLoader() {
        RequestListener glideRequestListener = new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                presenter.imageError(e.getLocalizedMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                presenter.imageReady();
                return false;
            }
        };
        imageLoader.setOnFinishedImageLoadingListener(glideRequestListener);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupInjection() {
        FacebookRecipesApp app = (FacebookRecipesApp) getApplication();
        component = app.getRecipeMainComponent(this, this);
        presenter = getPresenter();
        imageLoader = getImageLoader();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showUIElements() {
        //imgRecipe.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidUIElements() {
        //imgRecipe.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
    }

    private void clearImage() {
        imgRecipe.setImageResource(0);
    }

    @Override
    public void saveAnimation() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.save_animation);
        anim.setAnimationListener(getAnimationListener());
        imgRecipe.startAnimation(anim);
    }

    @Override
    public void dismissAnimation() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dismiss_animation);
        anim.setAnimationListener(getAnimationListener());
        imgRecipe.startAnimation(anim);
    }

    private Animation.AnimationListener getAnimationListener() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearImage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    @OnClick(R.id.imgKeep)
    @Override
    public void onKeep() {
        if (currentRecipe != null) {
            presenter.saveRecipe(currentRecipe);
        }
    }

    @OnClick(R.id.imgDismiss)
    @Override
    public void onDismiss() {
        presenter.dismissRecipe();
    }

    @Override
    public void onRecipeSaved() {
        Snackbar.make(layoutContainer, R.string.recipemain_notice_saved, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setRecipe(Recipe recipe) {
        this.currentRecipe = recipe;
        imageLoader.load(imgRecipe, recipe.getImageUrl());
    }

    @Override
    public void onGetRecipeError(String error) {
        String msgError = String.format(getString(R.string.recipemain_error), error);
        Snackbar.make(layoutContainer, msgError, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipes_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_list) {
            navigateToListScreen();
        } else if (id == R.id.action_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FacebookRecipesApp app = (FacebookRecipesApp) getApplication();
        app.logout();
    }

    private void navigateToListScreen() {
        startActivity(new Intent(this, RecipeListActivity.class));
    }

    public ImageLoader getImageLoader() {
        return component.getImageLoader();
    }

    public RecipeMainPresenter getPresenter() {
        return component.getPresenter();
    }


}
