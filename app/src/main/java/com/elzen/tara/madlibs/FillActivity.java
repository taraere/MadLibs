package com.elzen.tara.madlibs;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class FillActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
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

    public void placeHolder() {
        editText.setHint(story.getNextPlaceholder().toLowerCase());
    }

    public String wordCounter() {
        return "Only " + story.getPlaceholderRemainingCount() + " words to go!";
    }

    public String fileName() {
        return "madlib" + rnd.nextInt(5) + ".txt";
    }
}
