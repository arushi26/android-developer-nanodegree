package com.arushi.bakingapp.data.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class IngredientEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private float quantity;
    private String measure;
    private String ingredient;
    private int dessertId;

    public IngredientEntity(float quantity, String measure,
                         String ingredient, int dessertId){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.dessertId = dessertId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getDessertId() {
        return dessertId;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }
}
