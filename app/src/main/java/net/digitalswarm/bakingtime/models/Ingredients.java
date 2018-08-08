package net.digitalswarm.bakingtime.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Ingredients implements Parcelable {
    @SerializedName("quantity")
    private final String quantity;
    @SerializedName("measure")
    private final String measurement;
    @SerializedName("ingredient")
    private final String ingredient;

    //default constructor
    public Ingredients(String quantity, String measurement, String ingredient) {
        this.quantity = quantity;
        this.measurement = measurement;
        this.ingredient = ingredient;
    }
    //parcel constructor
    private Ingredients(Parcel parcel) {
        this.quantity = parcel.readString();
        this.measurement = parcel.readString();
        this.ingredient = parcel.readString();
    }
    //used for widget - format: Quantity[space]Measurement[tab]Ingredient
    @Override
    public String toString() {
        return quantity + " " + measurement + "\t" + ingredient;
    }

    //parcel object creator
    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //package object for parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getQuantity());
        parcel.writeString(this.getMeasurement());
        parcel.writeString(this.getIngredient());
    }

    //getters and setters
    public String getQuantity() {
        return quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getIngredient() {
        return ingredient;
    }
    //method to handle converting shared pref data into ingredient
    private static ArrayList<Ingredients> getIngredientsData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        //grab ingredientslist from shared pref
        Gson gson = new Gson();
        String jsonResponse = prefs.getString("INGREDIENTS_KEY", "Default String");
        ArrayList<Ingredients> ingredientsList = gson.fromJson(jsonResponse, new TypeToken<ArrayList<Ingredients>>(){}.getType());
        return ingredientsList;
    }
}
