package edu.galileo.android.tinderrecipes.recipelist;

import org.junit.Test;
import org.mockito.Mock;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.entities.Recipe;

import static org.mockito.Mockito.verify;

public class StoredRecipesInteractorImplTest extends BaseTest {

    @Mock
    private RecipeListRepository listRepository;

    @Mock
    private Recipe recipe;

    private StoredRecipesInteractorImpl listInteractor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        listInteractor = new StoredRecipesInteractorImpl(listRepository);
    }

    @Test
    public void testExecuteUpdate_callRepositoryUpdateRecipes() {
        listInteractor.executeUpdate(recipe);
        verify(listRepository).updateRecipe(recipe);
    }

    @Test
    public void testExecuteDelete_callRepositoryDeleteRecipe() {
        listInteractor.executeDelete(recipe);
        verify(listRepository).deleteRecipe(recipe);
    }
}