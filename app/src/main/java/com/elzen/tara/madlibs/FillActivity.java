package com.elzen.tara.madlibs;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class FillActivity extends AppCompatActivity {
    private final static String TAG = "FillActivity";

    Story story;
    EditText editText;
    TextView wordCount;
    final Random rnd = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill);
        editText = findViewById(R.id.edit_text);
        wordCount = findViewById(R.id.word_count);
        try {
            InputStream tale = getAssets().open(fileName());
            story = new Story(tale);

            TextView countDisplay = findViewById(R.id.word_count);
            countDisplay.setHint(wordCounter());
            placeHolder();
        }
        catch(IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public void goToNext(View view) {
        String text = editText.getText().toString();
        if (text.length() == 0) {
            Snackbar.make(view, "Please fill it in!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            story.fillInPlaceholder(text);
            story.getNextPlaceholder();
            if (story.isFilledIn()) {
                Intent intent = new Intent(this, StoryActivity.class);
                intent.putExtra("received_text", story.toString());
                startActivity(intent);
                finish();
            }
            editText.setText("");
            wordCount.setText(wordCounter());
            placeHolder();
        }
    }

    public void placeHolder() {
        editText.setHint(story.getNextPlaceholder().toLowerCase());
    }

    public String wordCounter() {
        return "Only " + story.getPlaceholderRemainingCount() + " words to go!";
    }

    public String fileName() {
        List names = new ArrayList();

        names.add("madlib0_Dance_Monstrosity.txt");
        names.add("madlib1_Simple_Life.txt");
        names.add("madlib2_Tarzan.txt");
        names.add("madlib3_American_Uni.txt");
        names.add("madlib4_Male_Clothing.txt");

        String item = (String) names.get(rnd.nextInt(5));
        return item;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        outState.putSerializable("coolStoryBruh", story);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onSaveInstanceState(inState);
        story = (Story) inState.getSerializable("coolStoryBruh");
        String text = editText.getText().toString();
        if (text.length() == 0) {
            placeHolder();
        }
        else {
            editText.setText(text);
        }
        wordCount.setText(wordCounter());

    }
}
