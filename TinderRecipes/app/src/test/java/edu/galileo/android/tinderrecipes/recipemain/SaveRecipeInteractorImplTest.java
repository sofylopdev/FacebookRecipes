package edu.galileo.android.tinderrecipes.recipemain;

import org.junit.Test;
import org.mockito.Mock;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.entities.Recipe;

import static org.mockito.Mockito.verify;

public class SaveRecipeInteractorImplTest extends BaseTest{

    @Mock
    private RecipeMainRepository repository;

    @Mock
    private Recipe recipe;

    private SaveRecipeInteractor interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new SaveRecipeInteractorImpl(repository);
    }

    @Test
    public void testExecute_callRepository() throws Exception{
        interactor.execute(recipe);
        verify(repository).saveRecipe(recipe);
    }
}
