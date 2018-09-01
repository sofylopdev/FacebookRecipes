package edu.galileo.android.tinderrecipes.recipemain;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.R;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.ImageLoader;
import edu.galileo.android.tinderrecipes.login.ui.LoginActivity;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.tinderrecipes.recipemain.ui.RecipeMainActivity;
import edu.galileo.android.tinderrecipes.recipemain.ui.RecipeMainView;
import edu.galileo.android.tinderrecipes.support.ShadowImageView;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27,
        shadows = {ShadowImageView.class})
public class RecipeMainActivityTest extends BaseTest {

    @Mock
    private Recipe currentRecipe;

    @Mock
    private RecipeMainPresenter presenter;
    @Mock
    private ImageLoader imageLoader;

    private RecipeMainView view;
    private RecipeMainActivity activity;
    private ActivityController<RecipeMainActivity> controller;

    private ShadowActivity shadowActivity;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RecipeMainActivity recipeMainActivity = new RecipeMainActivity() {
            public ImageLoader getImageLoader() {
                return imageLoader;
            }

            public RecipeMainPresenter getPresenter() {
                return presenter;
            }
        };

        controller = ActivityController.of(recipeMainActivity).create().visible();
        activity = controller.get();
        view = (RecipeMainActivity) activity;
        shadowActivity = shadowOf(activity);
    }

    @Test
    public void testOnActivityCreated_getNextRecipe() throws Exception {
        verify(presenter).onCreate();
        verify(presenter).getNextRecipe();
    }

    @Test
    public void testLogoutMenuClicked_ShouldLaunchLoginActivity() throws Exception {
        shadowActivity.clickMenuItem(R.id.action_logout);
        Intent intent = shadowActivity.peekNextStartedActivity();
        assertEquals(new ComponentName(activity, LoginActivity.class), intent.getComponent());
    }

    @Test
    public void testListMenuClicked_ShouldLaunchRecipeListActivity() throws Exception {
        shadowActivity.clickMenuItem(R.id.action_list);
        Intent intent = shadowActivity.peekNextStartedActivity();
        assertEquals(new ComponentName(activity, RecipeListActivity.class), intent.getComponent());
    }

    @Test
    public void testKeepButtonClicked_ShouldSaveRecipe() {
        activity.setRecipe(currentRecipe);
        ImageButton buttonKeep = activity.findViewById(R.id.imgKeep);
        buttonKeep.performClick();
        //test if the presenter's saveRecipe method got called (with the same recipe obj)
        verify(presenter).saveRecipe(currentRecipe);
    }

    @Test
    public void testDismissButtonClicked_ShouldDismissRecipe() {
        ImageButton buttonDismiss = activity.findViewById(R.id.imgDismiss);
        buttonDismiss.performClick();
        //test if the presenter's dismissRecipe method got called
        verify(presenter).dismissRecipe();
    }

    @Test
    public void testOnSwipeToKeep_ShouldSaveRecipe() {
        activity.setRecipe(currentRecipe);
        ImageView imgRecipe = activity.findViewById(R.id.imgRecipe);
        ShadowImageView shadowImage = (ShadowImageView) Shadow.extract(imgRecipe);
        shadowImage.performSwipe(200, 200, 500, 250, 50);
        //test if the presenter's saveRecipe method got called (with the same recipe obj)
        verify(presenter).saveRecipe(currentRecipe);
    }

    @Test
    public void testOnSwipeToDismiss_ShouldDiscardRecipe() {
        ImageView imgRecipe = activity.findViewById(R.id.imgRecipe);
        ShadowImageView shadowImage = Shadow.extract(imgRecipe);
        shadowImage.performSwipe(200, 200, -500, 250, 50);
        verify(presenter).dismissRecipe();
    }

    @Test
    public void testOnActivityDestroyed_destroyPresenter() throws Exception {
        controller.destroy();
        verify(presenter).onDestroy();
    }

    @Test
    public void testShowProgress_progressBarShouldBeVisible() throws Exception {
        view.showProgress();
        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        assertEquals(View.VISIBLE, progressBar.getVisibility());
    }

    @Test
    public void testHideProgress_progressBarShouldBeGone() throws Exception {
        view.hideProgress();
        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        assertEquals(View.GONE, progressBar.getVisibility());
    }

    @Test
    public void testShowUIElements_buttonsShouldBeVisible() throws Exception {
        view.showUIElements();
        LinearLayout linearLayout = activity.findViewById(R.id.linearLayout);
        assertEquals(View.VISIBLE, linearLayout.getVisibility());
    }

    @Test
    public void testHideUIElements_buttonsShouldBeGone() throws Exception {
        view.hidUIElements();
        LinearLayout linearLayout = activity.findViewById(R.id.linearLayout);
        assertEquals(View.GONE, linearLayout.getVisibility());
    }

    @Test
    public void testSaveAnimation_AnimationShouldBeStarted() throws Exception {
        view.saveAnimation();
        ImageView imgRecipe = activity.findViewById(R.id.imgRecipe);
        assertNotNull(imgRecipe.getAnimation());
        assertTrue(imgRecipe.getAnimation().hasStarted());
    }

    @Test
    public void testDismissAnimation_() throws Exception {
        view.dismissAnimation();
        ImageView imgRecipe = activity.findViewById(R.id.imgRecipe);
        assertNotNull(imgRecipe.getAnimation());
        assertTrue(imgRecipe.getAnimation().hasStarted());
    }

    @Test
    public void testSetRecipe_ImageLoaderShouldBeCalled() throws Exception {
        String url = "http://galileo.edu";
        when(currentRecipe.getImageUrl()).thenReturn(url);

        view.setRecipe(currentRecipe);
        ImageView imgRecipe = activity.findViewById(R.id.imgRecipe);
        verify(imageLoader).load(imgRecipe, currentRecipe.getImageUrl());
    }
}
