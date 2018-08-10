package net.digitalswarm.bakingtime.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RecipeSteps implements Parcelable {
    @SerializedName("id")
    private final String id;
    @SerializedName("shortDescription")
    private final String shortDescription;
    @SerializedName("description")
    private final String description;
    @SerializedName("videoURL")
    private final String videoUrl;
    @SerializedName("thumbnailURL")
    private final String thumbnailUrl;
    //default constructor
    public RecipeSteps(String id, String shortDescription,
                       String description, String videoUrl, String thumbnailUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    //parcel version constructor
    private RecipeSteps(Parcel parcel){
        this.id = parcel.readString();
        this.shortDescription = parcel.readString();
        this.description = parcel.readString();
        this.videoUrl = parcel.readString();
        this.thumbnailUrl = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //recipe steps object creator from parcel
    public static final Creator<RecipeSteps> CREATOR = new Creator<RecipeSteps>() {
        @Override
        public RecipeSteps createFromParcel(Parcel in) {
            return new RecipeSteps(in);
        }

        @Override
        public RecipeSteps[] newArray(int size) {
            return new RecipeSteps[size];
        }
    };

    //package object for parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getId());
        parcel.writeString(this.getShortDescription());
        parcel.writeString(this.getDescription());
        parcel.writeString(this.getVideoUrl());
        parcel.writeString(this.getThumbnailUrl());
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
