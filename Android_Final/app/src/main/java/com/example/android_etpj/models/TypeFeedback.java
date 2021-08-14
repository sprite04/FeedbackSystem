package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TypeFeedback implements Serializable {
    @SerializedName("TypeID")
    private int typeID;

    @SerializedName("TypeName")
    private String typeName;

    @SerializedName("isDeleted")
    private boolean IsDeleted;

    public TypeFeedback() {
    }

    public TypeFeedback(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeID() {
        return typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
