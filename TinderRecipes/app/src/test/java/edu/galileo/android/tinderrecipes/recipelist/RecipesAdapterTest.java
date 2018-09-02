package edu.galileo.android.tinderrecipes.recipelist;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.share.model.ShareContent;
import com.facebook.share.widget.SendButton;
import com.facebook.share.widget.ShareButton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import java.util.List;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.R;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.ImageLoader;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.RecipesAdapter;
import edu.galileo.android.tinderrecipes.support.ShadowRecyclerViewAdapter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27,
        shadows = {ShadowRecyclerViewAdapter.class})
public class RecipesAdapterTest extends BaseTest {

    @Mock
    private List<Recipe> recipeList;
    @Mock
    private ImageLoader imageLoader;
    @Mock
    private OnItemClickListener onItemClickListener;

    @Mock
    private Recipe recipe;

    private RecipesAdapter adapter;
    private ShadowRecyclerViewAdapter shadowAdapter;

    public static final String RECIPE_URL = "http://galileo.edu";

    @Override
    public void setUp() throws Exception {
        super.setUp();

        when(recipe.getSourceUrl()).thenReturn(RECIPE_URL);
        adapter = new RecipesAdapter(recipeList, imageLoader, onItemClickListener);
        shadowAdapter = (ShadowRecyclerViewAdapter) Shadow.extract(adapter);

        AppCompatActivity activity = Robolectric.setupActivity(AppCompatActivity.class);
        RecyclerView recyclerView = new RecyclerView(activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }

    @Test
    public void testSetRecipes_ItemCountMatches() {
        int itemCount = 5;
        when(recipeList.size()).thenReturn(itemCount);
        adapter.setRecipes(recipeList);

        assertEquals(itemCount, adapter.getItemCount());
    }

    @Test
    public void testRemoveRecipe_IsRemovedFromAdapter() {
        adapter.removeRecipe(recipe);
        verify(recipeList).remove(recipe);
    }

    @Test
    public void testOnItemClick_ShouldCallListener() {
        int positionToClick = 0;
        when(recipeList.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        //if:   shadowAdapter.itemVisible(positionToClick + 1) the test would fail
        shadowAdapter.performItemClick(positionToClick);
        verify(onItemClickListener).onItemClick(recipe);
    }

    @Test
    public void testViewHolder_ShouldRenderTitle() {
        int positionToShow = 0;
        String recipeTitle = "title";
        when(recipe.getTitle()).thenReturn(recipeTitle);
        when(recipeList.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);
        View view = shadowAdapter.getViewHolderPosition(positionToShow);
        TextView txtRecipeName = view.findViewById(R.id.txtRecipeName);

        assertEquals(recipeTitle, txtRecipeName.getText().toString());
    }

    @Test
    public void testOnFavoriteClick_ShouldCallListener() {
        int positionToClick = 0;
        boolean favorite = true;
        when(recipe.isFavorite()).thenReturn(favorite);
        when(recipeList.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.imgFav);

        View view = shadowAdapter.getViewHolderPosition(positionToClick);
        ImageButton imgFav = view.findViewById(R.id.imgFav);

        assertEquals(favorite, imgFav.getTag());

        verify(onItemClickListener).onFavClick(recipe);
    }

    @Test
    public void testOnNonFavoriteClick_ShouldCallListener() {
        int positionToClick = 0;
        boolean favorite = false;
        when(recipe.isFavorite()).thenReturn(favorite);
        when(recipeList.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.imgFav);

        View view = shadowAdapter.getViewHolderPosition(positionToClick);
        ImageButton imgFav = view.findViewById(R.id.imgFav);

        assertEquals(favorite, imgFav.getTag());

        verify(onItemClickListener).onFavClick(recipe);
    }

    @Test
    public void testOnDeleteClick_ShouldCallListener() {
        int positionToClick = 0;
        when(recipeList.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.imgDelete);

        verify(onItemClickListener).onDeleteClick(recipe);
    }

    @Test
    public void testFBShareBind_ShareContentSet() {
        int positionToShow = 0;
        when(recipeList.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);
        View view = shadowAdapter.getViewHolderPosition(positionToShow);
        ShareButton fbShare = view.findViewById(R.id.fbShare);
        ShareContent shareContent = fbShare.getShareContent();
        assertNotNull(shareContent);

        assertEquals(RECIPE_URL, shareContent.getContentUrl().toString());
    }

    @Test
    public void testFBSendBind_ShareContentSet() {
        int positionToShow = 0;
        when(recipeList.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);
        View view = shadowAdapter.getViewHolderPosition(positionToShow);
        SendButton fbSend = view.findViewById(R.id.fbSend);

        ShareContent shareContent = fbSend.getShareContent();
        assertNotNull(shareContent);
        assertEquals(RECIPE_URL, shareContent.getContentUrl().toString());
    }
}
