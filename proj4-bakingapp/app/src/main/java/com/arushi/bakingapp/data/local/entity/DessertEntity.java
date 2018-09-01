package com.arushi.bakingapp.data.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "desserts")
public class DessertEntity {
    @PrimaryKey
    private int id;

    private String image;
    private String name;

    private int servings;

    public DessertEntity(int id, String image,
                         String name, int servings){
        this.id = id;
        this.image = image;
        this.name = name;
        this.servings = servings;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }
}
