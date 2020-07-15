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

import net.alhazmy13.imagefilter.ImageFilter;

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

            case 17:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GRAY);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 18:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.RELIEF);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 19:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.AVERAGE_BLUR,9);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 20:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.OIL,10);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 21:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.NEON,200, 50, 100);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 22:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.PIXELATE,9);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 23:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.TV);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 24:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.INVERT);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 25:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.BLOCK);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 26:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.OLD);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 27:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SHARPEN);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 28:
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.LIGHT,width / 2, height / 2, Math.min(width / 2, height / 2));
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 29:
                double radius = (bitmap.getWidth() / 2) * 95 / 100;
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.LOMO,radius);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 30:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.HDR);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 31:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GAUSSIAN_BLUR,1.2);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 32:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SOFT_GLOW,0.6);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 33:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SKETCH);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 34:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.MOTION_BLUR,5,1);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            case 35:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GOTHAM);
            Toast.makeText(this, "Filter 15", Toast.LENGTH_SHORT).show();
            default:
        }
        if(count==16)
            count=0;
    }

}