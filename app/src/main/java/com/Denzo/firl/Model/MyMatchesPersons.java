package com.Denzo.firl.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.Denzo.firl.App;
import com.Denzo.firl.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyMatchesPersons {
    private static final String STORAGE = "shop";
    private SharedPreferences storage;

    private MyMatchesPersons (){
        storage = App.getInstance().getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
    }
    public static  MyMatchesPersons get(){
        return new MyMatchesPersons();
    }
    public List<MatchPerson> getData(){
        return Arrays.asList(
//                new MatchCourse(1, "Everyday Candle", "$12.00 USD", R.drawable.shop1),
//                new MatchCourse(2, "Small Porcelain Bowl", "$50.00 USD", R.drawable.shop2),
//                new MatchCourse(3, "Favourite Board", "$265.00 USD", R.drawable.shop3),
//                new MatchCourse(4, "Earthenware Bowl", "$18.00 USD", R.drawable.shop4),
//                new MatchCourse(5, "Porcelain Dessert Plate", "$36.00 USD", R.drawable.shop5),
//                new MatchCourse(6, "Detailed Rolling Pin", "$145.00 USD", R.drawable.shop6)
         //       new MatchPerson(1, "Digital Marketing", "12 courses available", R.drawable.education_2),
           //     new MatchPerson(2, "Business", "50 courses available", R.drawable.education_3),
             //   new MatchPerson(3, "Development", "265 courses available", R.drawable.education_4),
               // new MatchPerson(4, "Security", "18 courses available", R.drawable.education_1),
               // new MatchPerson(5, "Ethical Hacking", "36 courses available", R.drawable.education_5),
                //new MatchPerson(6, "Mobile", "145 courses available", R.drawable.education_6)
        );

    }

    public boolean isRated(int itemId){
        return storage.getBoolean(String.valueOf(itemId), false);
    }
    public void setRated(int itemId, boolean isRated){
        storage.edit().putBoolean(String.valueOf(itemId), isRated).apply();
    }
}
