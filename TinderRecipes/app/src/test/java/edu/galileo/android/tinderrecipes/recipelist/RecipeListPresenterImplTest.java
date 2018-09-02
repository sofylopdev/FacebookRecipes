package edu.galileo.android.tinderrecipes.recipelist;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;


import java.util.Arrays;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.EventBus;
import edu.galileo.android.tinderrecipes.recipelist.events.RecipeListEvent;
import edu.galileo.android.tinderrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.tinderrecipes.recipemain.events.RecipeMainEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeListPresenterImplTest extends BaseTest {

    @Mock
    private EventBus eventBus;
    @Mock
    private RecipeListView view;

    @Mock
    private RecipeListInteractor listInteractor;//get from db
    @Mock
    private StoredRecipesInteractor storedInteractor;//update or delete

    @Mock
    private Recipe recipe;

    @Mock
    private RecipeListEvent event;

    private RecipeListPresenterImpl presenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter = new RecipeListPresenterImpl(eventBus, view, listInteractor, storedInteractor);
    }

    @Test
    public void testOnCreate_subscribedToEventBus() {
        presenter.onCreate();
        verify(eventBus).register(presenter);
    }

    @Test
    public void testOnDestroy_UnsubscribedToEventBus() {
        presenter.onDestroy();
        verify(eventBus).unregister(presenter);
    }

    @Test
    public void testGetRecipes_ExecuteListInteractor() {
        presenter.getRecipes();
        verify(listInteractor).execute();
    }

    @Test
    public void testRemoveRecipe_ExecuteStoredInteractor() {
        presenter.removeRecipe(recipe);
        verify(storedInteractor).executeDelete(recipe);
    }

    @Test
    public void testToggleFavorite_True() {
        Recipe recipe = new Recipe();
        boolean favorite = true;
        recipe.setFavorite(favorite);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        presenter.toggleFavorite(recipe);
        verify(storedInteractor).executeUpdate(recipeArgumentCaptor.capture());

        assertEquals(!favorite, recipeArgumentCaptor.getValue().isFavorite());
    }

    @Test
    public void testToggleFavorite_False() {
        Recipe recipe = new Recipe();
        boolean favorite = false;
        recipe.setFavorite(favorite);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        presenter.toggleFavorite(recipe);
        verify(storedInteractor).executeUpdate(recipeArgumentCaptor.capture());

        assertEquals(!favorite, recipeArgumentCaptor.getValue().isFavorite());
    }

    @Test
    public void testOnReadEvent_SetHasRecipesOnView(){
        //configuring the mock:
        when(event.getType()).thenReturn(RecipeListEvent.READ_EVENT);
        when(event.getRecipeList()).thenReturn(Arrays.asList(recipe));

        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view).setRecipeList(Arrays.asList(recipe));
    }

    @Test
    public void testOnUpdateEvent_callUpdatedOnView(){
        //configuring the mock:
        when(event.getType()).thenReturn(RecipeListEvent.UPDATE_EVENT);

        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view).recipeUpdated();
    }

    @Test
    public void testOnDeleteEvent_RecipeDeletedOnView(){
        //configuring the mock:
        when(event.getType()).thenReturn(RecipeListEvent.DELETE_EVENT);
        when(event.getRecipeList()).thenReturn(Arrays.asList(recipe));

        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view).recipeDeleted(recipe);
    }

    @Test
    public void testGetView_returnsView(){

        assertEquals(view, presenter.getView());
    }
}
