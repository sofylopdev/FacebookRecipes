package edu.galileo.android.tinderrecipes.recipelist;

import org.junit.Test;
import org.mockito.Mock;

import edu.galileo.android.tinderrecipes.BaseTest;

import static org.mockito.Mockito.verify;

public class RecipeListInteractorImplTest extends BaseTest{

    @Mock
    private RecipeListRepository listRepository;

    private RecipeListInteractorImpl listInteractor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        listInteractor = new RecipeListInteractorImpl(listRepository);
    }

    @Test
    public void testExecute_callRepositoryGetSavedRecipes() {
        listInteractor.execute();
        verify(listRepository).getSavedRecipes();
    }
}
