package edu.galileo.android.tinderrecipes.recipelist.ui.adapters;

import edu.galileo.android.tinderrecipes.entities.Recipe;

public interface OnItemClickListener {
    void onFavClick(Recipe recipe);
    void onItemClick(Recipe recipe);
    void onDeleteClick(Recipe recipe);
}
