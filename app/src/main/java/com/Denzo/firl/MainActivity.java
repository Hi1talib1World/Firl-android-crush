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

    StoryView storyView;   // get the object for StoryView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        storyView = findViewById(R.id.storyView); // find the XML view using findViewById
        storyView.resetStoryVisits(); // reset the storyview

        ArrayList<StoryModel> StoriesList = new ArrayList<>();  // create a Array list of Stories
        StoriesList.add(new StoryModel("https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80","Status 1","Yesterday"));
        StoriesList.add(new StoryModel("https://www.bigstockphoto.com/images/homepage/module-6.jpg","Status 2","10:15 PM"));
        StoriesList.add(new StoryModel("https://www.gettyimages.com/gi-resources/images/500px/983794168.jpg","Satus 3","Today,2:31 PM"));
        storyView.setImageUris(StoriesList);  // finally set the stories to storyview
    }
}
