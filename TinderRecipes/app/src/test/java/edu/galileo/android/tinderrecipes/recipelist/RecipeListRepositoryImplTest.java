package edu.galileo.android.tinderrecipes.recipelist;

import com.raizlabs.android.dbflow.sql.language.Select;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.FacebookRecipesApp;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.entities.Recipe_Table;
import edu.galileo.android.tinderrecipes.libs.base.EventBus;
import edu.galileo.android.tinderrecipes.recipelist.events.RecipeListEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class RecipeListRepositoryImplTest extends BaseTest {

    @Mock
    private EventBus eventBus;

    @Mock
    private Recipe recipe;

    private FacebookRecipesApp app;

    private ArgumentCaptor<RecipeListEvent> recipeListEventArgumentCaptor;

    private RecipeListRepositoryImpl repository;

    public static final int RECIPES_IN_DELETE_EVENT = 1;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        repository = new RecipeListRepositoryImpl(eventBus);
        app = (FacebookRecipesApp) RuntimeEnvironment.application;
        recipeListEventArgumentCaptor = ArgumentCaptor.forClass(RecipeListEvent.class);
        app.onCreate();
    }

    @After
    public void tearDown() {
        app.onTerminate();
    }

    @Test
    public void testGetSavedRecipes_eventPosted() {
        int recipesToStore = 5;
        Recipe currentRecipe;
        List<Recipe> testRecipeList = new ArrayList<>();
        for (int i = 0; i < recipesToStore; i++) {
            currentRecipe = new Recipe();
            currentRecipe.setRecipeID("id " + i);
            currentRecipe.save();
            testRecipeList.add(currentRecipe);
        }

        //using the select followed by query because of the cache, can give exception
        List<Recipe> recipesFromDb = new Select()
                .from(Recipe.class)
                .queryList();

        repository.getSavedRecipes();

        verify(eventBus).post(recipeListEventArgumentCaptor.capture());
        RecipeListEvent event = recipeListEventArgumentCaptor.getValue();

        assertEquals(RecipeListEvent.READ_EVENT, event.getType());
        //this is possible because the class Recipe has the method 'equals' implemented
        assertEquals(recipesFromDb, event.getRecipeList());

        //I don't want to change the database, so i am deleting the test values from it
        for (Recipe recipe : testRecipeList) {
            recipe.delete();
        }
    }

    @Test
    public void testUpdateRecipe_eventPosted() {
        String newRecipeId = "id1";
        String titleBefore = "title before update";
        String titleAfter = "title after update";

        Recipe recipe = new Recipe();
        recipe.setRecipeID(newRecipeId);
        recipe.setTitle(titleBefore);
        recipe.save();
        recipe.setTitle(titleAfter);

        repository.updateRecipe(recipe);

        Recipe recipeFromDB = new Select()
                .from(Recipe.class)
                .where(Recipe_Table.recipeID.is(newRecipeId))
                .querySingle();

        assertEquals(titleAfter, recipeFromDB.getTitle());

        verify(eventBus).post(recipeListEventArgumentCaptor.capture());
        RecipeListEvent event = recipeListEventArgumentCaptor.getValue();

        assertEquals(RecipeListEvent.UPDATE_EVENT, event.getType());

        recipe.delete();
    }

    @Test
    public void testDeleteRecipe_eventPosted() {
        String newRecipeId = "id1";
        Recipe recipe = new Recipe();
        recipe.setRecipeID(newRecipeId);
        recipe.save();

        repository.deleteRecipe(recipe);
        assertFalse(recipe.exists());

        verify(eventBus).post(recipeListEventArgumentCaptor.capture());
        RecipeListEvent event = recipeListEventArgumentCaptor.getValue();

        assertEquals(RecipeListEvent.DELETE_EVENT, event.getType());

        assertEquals(RECIPES_IN_DELETE_EVENT, event.getRecipeList().size());
        assertEquals(recipe, event.getRecipeList().get(0));

        recipe.delete();
    }
}
