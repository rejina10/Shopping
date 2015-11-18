package com.android.shopping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by rejina on 11/12/2015.
 */
public class ItemModel implements Parcelable {


    /**
     * category : Decorations
     * created_at : 2015-11-11T22:47:23Z
     * id : 1172
     * name : Finger paints
     * updated_at : 2015-11-11T22:47:23Z
     * user_id : 63
     */

    private String category;
    private int headerId;
    private String created_at;
    private int id;
    private String name;
    private String updated_at;
    private int user_id;

    public ItemModel() {

    }

    public ItemModel(Parcel in) {
        readFromParcel(in);
    }

    public static ItemModel objectFromData(String str) {
        return new Gson().fromJson(str, ItemModel.class);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCategory() {
        return category;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(name);
        dest.writeInt(id);
    }

    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        category = in.readString();
        name = in.readString();
        id = in.readInt();

    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public ItemModel createFromParcel(Parcel in) {
                    return new ItemModel(in);
                }

                @Override
                public Object[] newArray(int i) {
                    return new Object[0];
                }
            };
}
