package com.snake.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String toJsonString(Object object){
        try {
             return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static  Object Parse(String jsonString, Class classType){
        try {
            return objectMapper.readValue(jsonString,classType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
