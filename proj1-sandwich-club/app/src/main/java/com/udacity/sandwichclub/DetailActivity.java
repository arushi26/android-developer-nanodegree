package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private Sandwich mSandwich;
    private TextView mTvMainName, mTvAlsoKnownAs, mTvOrigin, mTvDesc, mTvIngredients;
    private TextView mLblAlsoKnownAs, mLblIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        mSandwich = JsonUtils.parseSandwichJson(json);
        if (mSandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(mSandwich.getImage())
                .into(ingredientsIv);

        setTitle(mSandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        /* Name */
        mTvMainName = findViewById(R.id.name_tv);
        mTvMainName.setText(mSandwich.getMainName());

        /* Also Known As */
        mTvAlsoKnownAs = findViewById(R.id.also_known_tv);
        List<String> alsoKnownAs = mSandwich.getAlsoKnownAs();

        if (alsoKnownAs.size() > 0) {
            String textAlsoKnownAs = "";

            for (int i = 0; i < alsoKnownAs.size(); i++) {
                if (i > 0) {
                    textAlsoKnownAs = textAlsoKnownAs + ", ";
                }
                textAlsoKnownAs = textAlsoKnownAs + alsoKnownAs.get(i);
            }
            mTvAlsoKnownAs.setText(textAlsoKnownAs);

        } else { // data not available
            mTvAlsoKnownAs.setVisibility(View.GONE);
            mLblAlsoKnownAs = findViewById(R.id.also_known_lbl);
            mLblAlsoKnownAs.setVisibility(View.GONE);
        }

        /* Origin */
        mTvOrigin = findViewById(R.id.origin_tv);
        String origin = mSandwich.getPlaceOfOrigin();
        if(!TextUtils.isEmpty(origin)){ // data is available
            mTvOrigin.setText(origin);
        }

        /* Description */
        mTvDesc = findViewById(R.id.description_tv);
        String description = mSandwich.getDescription();
        if(!TextUtils.isEmpty(description)){ // data is available
            mTvDesc.setText(description);
        }

        /* Ingredients */
        mTvIngredients = findViewById(R.id.ingredients_tv);
        List<String> ingredients = mSandwich.getIngredients();

        if (ingredients.size() > 0) {
            String textIngredients = "";

            for (int i = 0; i < ingredients.size(); i++) {
                if (i > 0) {
                    textIngredients = textIngredients + "\n";
                }
                textIngredients = textIngredients + "* " + ingredients.get(i);
            }
            mTvIngredients.setText(textIngredients);

        } else { // data not available
            mTvIngredients.setVisibility(View.GONE);
            mLblIngredients = findViewById(R.id.ingredients_lbl);
            mLblIngredients.setVisibility(View.GONE);
        }
    }
}
