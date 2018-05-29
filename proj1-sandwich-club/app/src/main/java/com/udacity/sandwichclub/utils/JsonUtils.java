package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {
        try{
            /* List of keys in JSON for sandwich details */
            final String IMAGE = "image";
            final String NAME = "name";
            final String MAIN_NAME = "mainName";
            final String ALSO_KNOWN_AS = "alsoKnownAs";
            final String ORIGIN = "placeOfOrigin";
            final String DESC = "description";
            final String INGREDIENTS = "ingredients";

            /* Get JSON object representing the sandwich */
            JSONObject sandwichObject = new JSONObject(json);
            /* Get the JSON object containing the sandwich names */
            JSONObject sandwichNames = sandwichObject.getJSONObject(NAME);

            Sandwich sandwich = null;

            /* List of 'Also Known as' names */
            JSONArray alsoKnownAsArray = sandwichNames.getJSONArray(ALSO_KNOWN_AS);
            List<String> parsedAlsoKnownAs = new ArrayList<>();
            for (int i = 0; i < alsoKnownAsArray.length(); i++) {
                parsedAlsoKnownAs.add(alsoKnownAsArray.getString(i));
            }

            /* List of 'Ingredients' */
            JSONArray ingredientsArray = sandwichObject.getJSONArray(INGREDIENTS);
            List<String> parsedIngredients = new ArrayList<>();
            for (int i = 0; i < ingredientsArray.length(); i++) {
                parsedIngredients.add(ingredientsArray.getString(i));
            }

            /* Sandwich object to be returned */
            sandwich = new Sandwich(
                            sandwichNames.getString(MAIN_NAME),
                            parsedAlsoKnownAs,
                            sandwichObject.getString(ORIGIN),
                            sandwichObject.getString(DESC),
                            sandwichObject.getString(IMAGE),
                            parsedIngredients );
            return sandwich;
        } catch (JSONException e) {
            Log.e(TAG, "Error while JSON parsing", e);
            return null;
        }
    }
}
