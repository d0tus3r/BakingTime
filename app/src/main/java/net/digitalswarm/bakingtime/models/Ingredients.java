package net.digitalswarm.bakingtime.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

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
        return quantity + " " + measurement + "   " + ingredient;
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
}
