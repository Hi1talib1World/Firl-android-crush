package com.Denzo.firl.filterView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.Denzo.firl.R;
import com.Denzo.firl.filterView.db.TagsManager;

import me.gujun.android.taggroup.TagGroup;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TagEditorActivity extends AppCompatActivity {
    private TagGroup mTagGroup;
    private TagsManager mTagsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_editor);

        mTagsManager = TagsManager.getInstance(getApplicationContext());
        String[] tags = mTagsManager.getTags();

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setTags(tags);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tag_editor_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mTagsManager.updateTags(mTagGroup.getTags());
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_submit) {
            mTagGroup.submitTag();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        mTagsManager.updateTags(mTagGroup.getTags());
        super.onBackPressed();
    }
}