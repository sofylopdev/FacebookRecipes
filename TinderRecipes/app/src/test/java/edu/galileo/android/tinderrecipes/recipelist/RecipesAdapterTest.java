package edu.galileo.android.tinderrecipes.recipelist;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import java.util.List;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.ImageLoader;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.tinderrecipes.recipelist.ui.adapters.RecipesAdapter;
import edu.galileo.android.tinderrecipes.support.ShadowRecyclerViewAdapter;

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

    @Override
    public void setUp() throws Exception {
        super.setUp();

        when(recipe.getSourceUrl()).thenReturn("http://galileo.edu");
        adapter = new RecipesAdapter(recipeList, imageLoader, onItemClickListener);
        shadowAdapter = (ShadowRecyclerViewAdapter) Shadow.extract(adapter);

        AppCompatActivity activity = Robolectric.setupActivity(AppCompatActivity.class);
        RecyclerView recyclerView = new RecyclerView(activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }
}
