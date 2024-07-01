package com.example.food.util;

import com.example.food.bean.Fruit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class JsonParse {
    private static JsonParse instance;
    private JsonParse() {
    }
    public static JsonParse getInstance() {
        if (instance == null) {
            instance = new JsonParse();
        }
        return instance;
    }
    public List<Fruit> getFruitList(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Fruit>>() {
        }.getType();

        List<Fruit> fruitList = gson.fromJson(json, listType);
        return fruitList;
    }
}

