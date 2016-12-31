package com.add.toeic.utils.json;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;
import android.util.Log;

import com.add.toeic.model.practice.ListenShort;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by sev_user on 12/29/2016.
 */

public class JsonListenShortUtils {


    public static final String TABLE_TAB = "Questions";
    public static final String VALUE_ANSWER = "answer";
    public static final String VALUE_POINTS = "points";
    public static final String VALUE_DURATION = "duration_in_seconds";
    public static final String VALUE_URL_PHOTO = "url_photo";
    public static final String VALUE_URL_AUDIO = "url_audio";
    public static final String VALUE_NEGATIVE = "negative_points";
    public static final String VALUE_OPTIONS = "options";
    public static final String VALUE_QUESTION_TYPE = "question_type";


    public static String readeJsonText(Context context, String nameJson) {
        try {
            String jsonLocation = AssetJSONFile(nameJson, context);
            JSONObject json = (JSONObject) new JSONTokener(jsonLocation).nextValue();
            JSONArray json2 = json.getJSONArray(TABLE_TAB);
            return json2.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        String s = new String(formArray);
        return s;
    }

    public static ArrayList<ListenShort> readerOjectFromJson(Context context, String nameJson) throws IOException {

        String jsontext = readeJsonText(context, nameJson);
        JsonReader jsonReader = new JsonReader(new StringReader(jsontext));
        return readObjectControlFromJson(jsonReader);
    }

    public static ArrayList<ListenShort> readObjectControlFromJson(JsonReader reader) throws IOException {
        ArrayList<ListenShort> messages = new ArrayList<ListenShort>();
        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }


    public static ListenShort readMessage(JsonReader reader) throws IOException {

        ListenShort listenShort = new ListenShort();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(VALUE_ANSWER)) {
                listenShort.setAnswer(reader.nextInt());
            } else if (name.equals(VALUE_POINTS)) {
                listenShort.setPoints(reader.nextInt());
            } else if (name.equals(VALUE_DURATION)) { // && reader.peek() != JsonToken.NULL) {
                listenShort.setDuration_in_seconds(reader.nextInt());
            } else if (name.equals(VALUE_URL_PHOTO)) {
                listenShort.setUrl_photo(reader.nextString());
            } else if (name.equals(VALUE_URL_AUDIO)) {
                listenShort.setUrl_audio(reader.nextString());
            } else if (name.equals(VALUE_NEGATIVE)) {
                listenShort.setNegative_points(reader.nextInt());
            } else if (name.equals(VALUE_OPTIONS)) {
                String[] strings = new String[4];
                reader.beginArray();
                int count = 0;
                while (reader.hasNext()) {
                    strings[count] = reader.nextString();
                    count++;
                }
                listenShort.setOptions(strings);
                reader.endArray();

            } else if (name.equals(VALUE_QUESTION_TYPE)) {
                listenShort.setQuestion_type(reader.nextInt());
            }
        }
        reader.endObject();
        return listenShort;
    }

}
