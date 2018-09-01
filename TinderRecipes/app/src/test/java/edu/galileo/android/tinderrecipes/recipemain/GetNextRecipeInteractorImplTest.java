package edu.galileo.android.tinderrecipes.recipemain;


import org.junit.Test;
import org.mockito.Mock;

import edu.galileo.android.tinderrecipes.BaseTest;

import static org.mockito.Mockito.verify;

public class GetNextRecipeInteractorImplTest extends BaseTest {

    @Mock
    private RecipeMainRepository repository;

    private GetNextRecipeInteractor interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new GetNextRecipeInteractorImpl(repository);
    }

    @Test
    public void testExecute_callRepository() throws Exception{
        interactor.execute();
        verify(repository).getNextRecipe();
    }
}
