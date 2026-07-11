package com.Denzo.firl.Model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ConvertersTest {

    @Test
    public void testListToString() {
        List<String> list = Arrays.asList("A", "B", "C");
        String result = Converters.fromList(list);
        assertEquals("[\"A\",\"B\",\"C\"]", result);
    }

    @Test
    public void testStringToList() {
        String json = "[\"A\",\"B\",\"C\"]";
        List<String> result = Converters.fromString(json);
        assertEquals(3, result.size());
        assertEquals("A", result.get(0));
    }

    @Test
    public void testMapToString() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        String result = Converters.fromMap(map);
        assertEquals("{\"key\":\"value\"}", result);
    }

    @Test
    public void testStringToMap() {
        String json = "{\"key\":\"value\"}";
        Map<String, String> result = Converters.fromMapString(json);
        assertEquals("value", result.get("key"));
    }
}
