package edu.galileo.android.tinderrecipes.recipemain;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.api.RecipeSearchResponse;
import edu.galileo.android.tinderrecipes.api.RecipesService;
import edu.galileo.android.tinderrecipes.entities.Recipe;
import edu.galileo.android.tinderrecipes.libs.base.EventBus;
import edu.galileo.android.tinderrecipes.recipemain.events.RecipeMainEvent;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeMainRepositoryImplTest extends BaseTest {

    @Mock
    private EventBus eventBus;
    @Mock
    private RecipesService service;

    @Mock
    Recipe recipe;

    private RecipeMainRepository repository;

    private ArgumentCaptor<RecipeMainEvent> recipeMainEventArgumentCaptor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        repository = new RecipeMainRepositoryImpl(eventBus, service);
        recipeMainEventArgumentCaptor = ArgumentCaptor.forClass(RecipeMainEvent.class);
    }

    @Test
    public void testSaveRecipeCalled_eventPosted() throws Exception {
        when(recipe.exists()).thenReturn(true);
        repository.saveRecipe(recipe);

        //when this event has been executed, we capture the call
        verify(eventBus).post(recipeMainEventArgumentCaptor.capture());
        //and then we can assign it to a new variable:
        RecipeMainEvent event = recipeMainEventArgumentCaptor.getValue();

        assertEquals(RecipeMainEvent.SAVE_EVENT, event.getType());
        assertNull(event.getError());
        assertNull(event.getRecipe());
    }


    private Call<RecipeSearchResponse> buildCall(final boolean success, final String errorMsg) {
        Call<RecipeSearchResponse> response = new Call<RecipeSearchResponse>() {
            @Override
            public Response<RecipeSearchResponse> execute() throws IOException {
                Response<RecipeSearchResponse> result = null;

                if (success) {
                    RecipeSearchResponse response = new RecipeSearchResponse();
                    response.setCount(1);
                    response.setRecipes(Arrays.asList(recipe));
                    result = Response.success(response);
                } else {
                    result = Response.error(null, null);
                }
                return result;
            }

            @Override
            public void enqueue(Callback<RecipeSearchResponse> callback) {
                if (success) {
                    try {
                        callback.onResponse(this, execute());
                    } catch (IOException e) {
                       e.printStackTrace();
                    }
                } else {
                    callback.onFailure(this, new Throwable(errorMsg));
                }
            }

            @Override
            public boolean isExecuted() {
                return true;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<RecipeSearchResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
        return response;
    }

    @Test
    public void testGetNextRecipeCalled_APIServiceSuccessCall_EventPosted() {

        int recipePage = new Random().nextInt(RecipeMainRepository.RECIPE_RANGE);
        when(service.search(BuildConfig.FOOD_API_KEY,
                RecipeMainRepository.RECENT_SORT,
                RecipeMainRepository.COUNT,
                recipePage))
                .thenReturn(buildCall(true, null));

        repository.setRecipePage(recipePage);
        repository.getNextRecipe();

        verify(service).search(BuildConfig.FOOD_API_KEY,
                RecipeMainRepository.RECENT_SORT,
                RecipeMainRepository.COUNT,
                recipePage);
        verify(eventBus).post(recipeMainEventArgumentCaptor.capture());
        RecipeMainEvent event = recipeMainEventArgumentCaptor.getValue();

        assertEquals(RecipeMainEvent.NEXT_EVENT, event.getType());
        assertNull(event.getError());
        assertNotNull(event.getRecipe());
        assertEquals(recipe, event.getRecipe());
    }

    @Test
    public void testGetNextRecipeCalled_APIServiceFailedCall_EventPosted() {
        String errorMsg = "error";
        int recipePage = new Random().nextInt(RecipeMainRepository.RECIPE_RANGE);
        when(service.search(BuildConfig.FOOD_API_KEY,
                RecipeMainRepository.RECENT_SORT,
                RecipeMainRepository.COUNT,
                recipePage))
                .thenReturn(buildCall(false, errorMsg));

        repository.setRecipePage(recipePage);
        repository.getNextRecipe();

        verify(service).search(BuildConfig.FOOD_API_KEY,
                RecipeMainRepository.RECENT_SORT,
                RecipeMainRepository.COUNT,
                recipePage);
        verify(eventBus).post(recipeMainEventArgumentCaptor.capture());
        RecipeMainEvent event = recipeMainEventArgumentCaptor.getValue();

        assertEquals(RecipeMainEvent.NEXT_EVENT, event.getType());
        assertNotNull(event.getError());
        assertNull(event.getRecipe());
        assertEquals(errorMsg, event.getError());
    }
}
