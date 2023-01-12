package com.app.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class JSON<T> {

    public T fromJson(String requestMessage, Class<T> objectClass) throws JsonProcessingException {
        return (new ObjectMapper()).readValue(requestMessage,objectClass);
    }

    public String toJson(T object) throws IOException {
        StringWriter stringWriter=new StringWriter();
         (new ObjectMapper()).writeValue(stringWriter,object);
        return stringWriter.toString();
    }

    public ArrayList<T> fromJsonArray(String requestMessage,Class<T> objectClass) throws JsonProcessingException {
        ArrayList<T> arrayList=new ArrayList<>();
        JSONArray jsonArray=(JSONArray) new JSONTokener(requestMessage).nextValue();
        for (int i = 0; i < jsonArray.length(); i++) {
            arrayList.add(new ObjectMapper().readValue(jsonArray.get(i).toString(),objectClass));

        }
        return arrayList;
    }


}
