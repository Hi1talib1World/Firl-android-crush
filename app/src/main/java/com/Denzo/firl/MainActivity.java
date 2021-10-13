package com.Denzo.firl;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;

import xute.storyview.StoryModel;
import xute.storyview.StoryView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StoryView storyView = findViewById(R.id.storyView);
        storyView.resetStoryVisits();
        ArrayList<StoryModel> uris = new ArrayList<>();
        uris.add(new StoryModel("https://picsum.photos/200/300", "Ankit Kumar", "12:00 PM"));
        uris.add(new StoryModel("https://picsum.photos/200/300", "Ankit Kumar", "12:00 PM"));
        uris.add(new StoryModel("https://picsum.photos/200/300", "Ankit Kumar", "12:00 PM"));
        uris.add(new StoryModel("https://picsum.photos/200/300", "Ankit Kumar", "12:00 PM"));
    }
}
