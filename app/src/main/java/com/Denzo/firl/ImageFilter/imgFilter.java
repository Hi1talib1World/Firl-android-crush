package com.Denzo.firl.ImageFilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Denzo.firl.R;
import com.uvstudio.him.photofilterlibrary.PhotoFilter;

public class imgFilter extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    ImageView imageView;
    Bitmap iBitmap,oBitmap;
    PhotoFilter photoFilter;
    int count=0;
    private Bitmap bitmap;
    private ImageView originalImage,filteredImage;
    private Spinner filterSpiner;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgfilter);
        initView();

    }
    private void initView() {

        originalImage = (ImageView) findViewById(R.id.imageView);
        filteredImage = (ImageView) findViewById(R.id.filteredImageView);
        filterSpiner = (Spinner) findViewById(R.id.filtersSpiner);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.image);
        originalImage.setImageBitmap(bitmap);
        filteredImage.setImageBitmap(ImageFilter.applyFilter(bitmap, ImageFilter.Filter.AVERAGE_BLUR));
        filterSpiner.setOnItemSelectedListener(this);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        count++;
        switch (count) {
            case 1:
                imageView.setImageBitmap(photoFilter.one(this, iBitmap));
                Toast.makeText(this, "Filter 1", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                imageView.setImageBitmap(photoFilter.two(this, iBitmap));
                Toast.makeText(this, "Filter 2", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                imageView.setImageBitmap(photoFilter.three(this, iBitmap));
                Toast.makeText(this, "Filter 3", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                imageView.setImageBitmap(photoFilter.four(this, iBitmap));
                Toast.makeText(this, "Filter 4", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                imageView.setImageBitmap(photoFilter.five(this, iBitmap));
                Toast.makeText(this, "Filter 5", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                imageView.setImageBitmap(photoFilter.six(this, iBitmap));
                Toast.makeText(this, "Filter 6", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                imageView.setImageBitmap(photoFilter.seven(this, iBitmap));
                Toast.makeText(this, "Filter 7", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                imageView.setImageBitmap(photoFilter.eight(this, iBitmap));
                Toast.makeText(this, "Filter 8", Toast.LENGTH_SHORT).show();
                break;
            case 9:
                imageView.setImageBitmap(photoFilter.nine(this, iBitmap));
                Toast.makeText(this, "Filter 9", Toast.LENGTH_SHORT).show();
                break;
            case 10:
                imageView.setImageBitmap(photoFilter.ten(this, iBitmap));
                Toast.makeText(this, "Filter 10", Toast.LENGTH_SHORT).show();
                break;
            case 11:
                imageView.setImageBitmap(photoFilter.eleven(this, iBitmap));
                Toast.makeText(this, "Filter 11", Toast.LENGTH_SHORT).show();
                break;
            case 12:
                imageView.setImageBitmap(photoFilter.twelve(this, iBitmap));
                Toast.makeText(this, "Filter 12", Toast.LENGTH_SHORT).show();
                break;
            case 13:
                imageView.setImageBitmap(photoFilter.thirteen(this, iBitmap));
                Toast.makeText(this, "Filter 13", Toast.LENGTH_SHORT).show();
                break;
            case 14:
                imageView.setImageBitmap(photoFilter.fourteen(this, iBitmap));
                Toast.makeText(this, "Filter 14", Toast.LENGTH_SHORT).show();
                break;
            case 15:
                imageView.setImageBitmap(photoFilter.fifteen(this, iBitmap));
                Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
                break;
            case 16:
                imageView.setImageBitmap(photoFilter.sixteen(this, iBitmap));
                Toast.makeText(this, "Filter 16", Toast.LENGTH_SHORT).show();
                break;
        }
        if(count==16)
            count=0;
    }

}