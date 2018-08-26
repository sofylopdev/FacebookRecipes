package edu.galileo.android.tinderrecipes.entities;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import edu.galileo.android.tinderrecipes.db.RecipesDatabase;

@Table(database = RecipesDatabase.class)
public class Recipe extends BaseModel {

    @SerializedName("recipe_id")//name in the api
    @PrimaryKey
    private String recipeID;

    @Column
    private String title;

    @SerializedName("image_url")
    @Column
    private String imageUrl;

    @SerializedName("source_url")
    @Column
    private String sourceUrl;

    @Column
    private boolean favorite;

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean equals(Object obj){
        boolean equal = false;
        if(obj instanceof Recipe){
            Recipe recipe = (Recipe) obj;
            equal = this.recipeID.equals(recipe.getRecipeID());
        }
        return equal;
    }
}
