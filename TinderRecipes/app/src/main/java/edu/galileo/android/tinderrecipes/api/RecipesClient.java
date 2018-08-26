package edu.galileo.android.tinderrecipes.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesClient {

    private Retrofit retrofit;
    public static final String BASE_URL = "http://food2fork.com/api/";

    public RecipesClient() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RecipesService getRecipesService(){
        return this.retrofit.create(RecipesService.class);
    }
}
