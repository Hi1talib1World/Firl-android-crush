package com.Denzo.firl.Model;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Converters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<String> fromString(String value) {
        if (value == null) return Collections.emptyList();
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static Map<String, String> fromMapString(String value) {
        if (value == null) return Collections.emptyMap();
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        return gson.fromJson(value, mapType);
    }

    @TypeConverter
    public static String fromMap(Map<String, String> map) {
        return gson.toJson(map);
    }
}
