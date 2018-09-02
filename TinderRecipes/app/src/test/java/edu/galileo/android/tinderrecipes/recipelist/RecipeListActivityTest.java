package edu.galileo.android.tinderrecipes.recipelist;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.facebook.FacebookActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import java.util.List;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.R;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.ImageLoader;
import edu.galileo.android.tinderrecipes.login.ui.LoginActivity;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.RecipesAdapter;
import edu.galileo.android.tinderrecipes.recipemain.ui.RecipeMainActivity;
import edu.galileo.android.tinderrecipes.support.ShadowRecyclerView;
import edu.galileo.android.tinderrecipes.support.ShadowRecyclerViewAdapter;

import static edu.galileo.android.tinderrecipes.recipelist.RecipesAdapterTest.RECIPE_URL;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27,
        shadows = {ShadowRecyclerView.class, ShadowRecyclerViewAdapter.class})
public class RecipeListActivityTest extends BaseTest {

    @Mock
    private RecipesAdapter adapter;
    @Mock
    private RecipeListPresenter presenter;

    @Mock
    private List<Recipe> recipeList;

    @Mock
    private Recipe recipe;

    @Mock
    private ImageLoader imageLoader;

    private OnItemClickListener onItemClickListener;

    private RecipeListView view;
    private ActivityController<RecipeListActivity> controller;

    private ShadowActivity shadowActivity;
    private RecipeListActivity activity;

    private ShadowRecyclerViewAdapter shadowAdapter;

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
        activity = controller.get();
        view = activity;
        onItemClickListener = activity;

        shadowActivity = shadowOf(activity);
    }


    @Test
    public void testLogoutMenuClicked_ShouldLaunchLoginActivity() {
        shadowActivity.clickMenuItem(R.id.action_logout);
        Intent intent = shadowActivity.peekNextStartedActivity();
        assertEquals(new ComponentName(activity, LoginActivity.class), intent.getComponent());
    }

    @Test
    public void testMainMenuClicked_ShouldLaunchRecipeMainActivity() {
        shadowActivity.clickMenuItem(R.id.action_main);
        Intent intent = shadowActivity.peekNextStartedActivity();
        assertEquals(new ComponentName(activity, RecipeMainActivity.class), intent.getComponent());
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

    @Test
    public void testOnRecyclerViewScroll_RecyclerViewShouldChangeScrollPosition() {
        int scrollPosition = 1;

        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        ShadowRecyclerView shadowRecyclerView = Shadow.extract(recyclerView);

        recyclerView.smoothScrollToPosition(scrollPosition);
        assertEquals(scrollPosition, shadowRecyclerView.getSmoothScrollPosition());
    }

    @Test
    public void testOnToolbarClicked_RecyclerViewShouldScrollToTop() {
        int scrollPosition = 1;
        int topScrollPosition = 0;

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);

        ShadowRecyclerView shadowRecyclerView = Shadow.extract(recyclerView);
        recyclerView.smoothScrollToPosition(scrollPosition);

        toolbar.performClick();
        assertEquals(topScrollPosition, shadowRecyclerView.getSmoothScrollPosition());
    }

    @Test
    public void testRecyclerViewItemClicked_ShouldStartViewActivity() {
        int positionToClick = 0;
        setupShadowAdapter(positionToClick);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClick(positionToClick);

        Intent intent = shadowActivity.peekNextStartedActivity();
        assertEquals(Intent.ACTION_VIEW, intent.getAction());
        assertEquals(recipeList.get(positionToClick).getSourceUrl(), intent.getDataString());
    }

    private void setupShadowAdapter(int positionToClick) {
        when(recipe.getSourceUrl()).thenReturn(RECIPE_URL);
        when(recipeList.get(positionToClick)).thenReturn(recipe);

        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        //The mock doesn't provide all that i need, so i need a real obj:
        RecipesAdapter adapterPopulated = new RecipesAdapter(recipeList, imageLoader, onItemClickListener);
        recyclerView.setAdapter(adapterPopulated);

        shadowAdapter = Shadow.extract(recyclerView.getAdapter());
    }

    @Test
    public void testRecyclerViewFavoriteClicked_ShouldCallPresenter() {
        int positionToClick = 0;
        setupShadowAdapter(positionToClick);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.imgFav);

        verify(presenter).toggleFavorite(recipe);
    }

    @Test
    public void testRecyclerViewRemoveClicked_ShouldCallPresenter() {
        int positionToClick = 0;
        setupShadowAdapter(positionToClick);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.imgDelete);

        verify(presenter).removeRecipe(recipe);
    }

    @Test
    public void testRecyclerViewFBShareClicked_ShouldStartFBActivity() {
        int positionToClick = 0;
        setupShadowAdapter(positionToClick);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.fbShare);

        Intent intent = shadowActivity.peekNextStartedActivity();
        assertEquals(new ComponentName(RuntimeEnvironment.application, FacebookActivity.class), intent.getComponent());
    }

    @Test
    public void testRecyclerViewFBSendClicked_ShouldStartFBActivity() {
        int positionToClick = 0;
        setupShadowAdapter(positionToClick);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.fbSend);

        Intent intent = shadowActivity.peekNextStartedActivity();
        assertEquals(new ComponentName(RuntimeEnvironment.application, FacebookActivity.class), intent.getComponent());
    }
}
