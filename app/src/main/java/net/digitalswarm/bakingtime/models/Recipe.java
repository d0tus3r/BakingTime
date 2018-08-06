package net.digitalswarm.bakingtime.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Recipe object holds all data associated to the recipe. Arrays for ingredients and steps
 * pojo for gson parsing
 */

public class Recipe implements Parcelable {
    @SerializedName("id")
    private final String id;
    @SerializedName("name")
    private final String name;
    @SerializedName("ingredients")
    private final ArrayList<Ingredients> ingredients;
    @SerializedName("steps")
    private final ArrayList<RecipeSteps> recipeSteps;
    @SerializedName("servings")
    private final String servings;
    @SerializedName("image")
    private final String imageId;


    public Recipe(String id, String name, ArrayList<Ingredients> ingredients,
                  ArrayList<RecipeSteps> recipeSteps, String servings, String imageId) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.recipeSteps = recipeSteps;
        this.servings = servings;
        this.imageId = imageId;
    }

    //parcel constructor
    private Recipe(Parcel parcel){
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.ingredients = parcel.createTypedArrayList(Ingredients.CREATOR);
        this.recipeSteps = parcel.createTypedArrayList(RecipeSteps.CREATOR);
        this.servings = parcel.readString();
        this.imageId = parcel.readString();
    }

    //recipe object creator from parcel
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //package object for parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getId());
        parcel.writeString(this.getName());
        parcel.writeTypedList(this.getIngredients());
        parcel.writeTypedList(this.getRecipeSteps());
        parcel.writeString(this.getServings());
        parcel.writeString(this.getImageId());
    }


    private String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public ArrayList<RecipeSteps> getRecipeSteps() {
        return recipeSteps;
    }

    private String getServings() {
        return servings;
    }

    private String getImageId() {
        return imageId;
    }
}
