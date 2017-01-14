package com.add.toeic.utils.json;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;

import com.add.toeic.model.practice.ListenShort;
import com.add.toeic.model.practice.QuestionResponse;

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

public class JsonQuestionResponseUtils {


    public static final String TABLE_TAB = "Questions";
    public static final String VALUE_ANSWER = "answer";
    public static final String VALUE_POINTS = "points";
    public static final String VALUE_DURATION = "duration_in_seconds";
    public static final String VALUE_URL_AUDIO = "url_audio";
    public static final String VALUE_NEGATIVE = "negative_points";
    public static final String VALUE_QUESTION = "question";
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
        String PATH_ASSETS = "practice/";
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(PATH_ASSETS + filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        String s = new String(formArray);
        return s;
    }

    public static ArrayList<QuestionResponse> readerOjectFromJson(Context context, String nameJson) throws IOException {

        String jsontext = readeJsonText(context, nameJson);
        JsonReader jsonReader = new JsonReader(new StringReader(jsontext));
        return readObjectControlFromJson(jsonReader);
    }

    public static ArrayList<QuestionResponse> readObjectControlFromJson(JsonReader reader) throws IOException {
        ArrayList<QuestionResponse> messages = new ArrayList<QuestionResponse>();
        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }


    public static QuestionResponse readMessage(JsonReader reader) throws IOException {

        QuestionResponse questionResponse = new QuestionResponse();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(VALUE_ANSWER)) {
                questionResponse.setAnswer(reader.nextInt());
            } else if (name.equals(VALUE_POINTS)) {
                questionResponse.setPoints(reader.nextInt());
            } else if (name.equals(VALUE_DURATION)) { // && reader.peek() != JsonToken.NULL) {
                questionResponse.setDuration_in_seconds(reader.nextInt());
            } else if (name.equals(VALUE_URL_AUDIO)) {
                questionResponse.setUrl_audio(reader.nextString());
            } else if (name.equals(VALUE_NEGATIVE)) {
                questionResponse.setNegative_points(reader.nextInt());
            } else if (name.equals(VALUE_QUESTION)) {
                questionResponse.setQuestion(reader.nextString());
            } else if (name.equals(VALUE_OPTIONS)) {
                String[] strings = new String[3];
                reader.beginArray();
                int count = 0;
                while (reader.hasNext()) {
                    strings[count] = reader.nextString();
                    count++;
                }
                questionResponse.setOptions(strings);
                reader.endArray();

            } else if (name.equals(VALUE_QUESTION_TYPE)) {
                questionResponse.setQuestion_type(reader.nextInt());
            }
        }
        reader.endObject();
        return questionResponse;
    }

}
