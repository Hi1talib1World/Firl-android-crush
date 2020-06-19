package com.Denzo.firl.Profil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.Denzo.firl.R;

import butterknife.BindView;
import jp.wasabeef.blurry.Blurry;

public class ProfilActivity extends AppCompatActivity {
    @BindView(R.id.ll) LinearLayout LinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageView Iv = (ImageView) findViewById(R.id.img1);

        Blurry.with(getApplicationContext())
                .radius(25)
                .async()
                .animate(500)
                .sampling(8)
                .onto(LinearLayout);
    }
}
