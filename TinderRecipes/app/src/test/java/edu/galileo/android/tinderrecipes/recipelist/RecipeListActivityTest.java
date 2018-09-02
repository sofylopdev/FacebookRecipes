package edu.galileo.android.tinderrecipes.recipelist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.List;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.R;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.RecipesAdapter;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class RecipeListActivityTest extends BaseTest {

    @Mock
    private RecipesAdapter adapter;
    @Mock
    private RecipeListPresenter presenter;

    @Mock
    private List<Recipe> recipeList;

    @Mock
    private Recipe recipe;

    private RecipeListView view;
    private ActivityController<RecipeListActivity> controller;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RecipeListActivity recipeListActivity = new RecipeListActivity() {
            //Because Robolectric uses the theme in manifest
            @Override
            public void setTheme(int resid) {
                super.setTheme(R.style.AppTheme_NoActionBar);
            }

            @Override
            public RecipesAdapter getAdapter() {
                return adapter;
            }

            @Override
            public RecipeListPresenter getPresenter() {
                return presenter;
            }
        };

        controller = ActivityController.of(recipeListActivity).create().visible();
        view = controller.get();
    }

    @Test
    public void testOnCreate_ShouldCallPresenter() {
        verify(presenter).onCreate();
        verify(presenter).getRecipes();
    }

    @Test
    public void testOnDestroy_ShouldCallPresenter() {
        controller.destroy();
        verify(presenter).onDestroy();
    }

    @Test
    public void testSetRecipe_ShouldSetInAdapter() {
        view.setRecipeList(recipeList);
        verify(adapter).setRecipes(recipeList);
    }

    @Test
    public void testRecipeUpdated_ShouldUpdateAdapter() {
        view.recipeUpdated();
        verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void testRecipeDeleted_ShouldUpdateAdapter() {
        view.recipeDeleted(recipe);
        verify(adapter).removeRecipe(recipe);
    }
}
