package com.jasper.microquizz.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Musea {
    private ArrayList<String> musea;
    private Object quiz;

    public Musea() {

    }

    public Musea(Map<String, Object> values) {
        JSONObject topobj = new JSONObject();

        for (String i : values.keySet()) {
            Object quizz = ((HashMap<String, Object>) values.get(i)).get("objecten");

            HashMap museaList = ((HashMap) ((ArrayList) quizz).get(0));
            HashMap quizList = ((HashMap) museaList.get("quiz"));

            String objName = museaList.get("naam").toString();

            String answerid = quizList.get("answerid").toString();
            String answer = quizList.get("answer").toString();
            String question = quizList.get("question").toString();
            String choice3 = quizList.get("choice3").toString();
            String choice2 = quizList.get("choice2").toString();
            String choice1 = quizList.get("choice1").toString();

            JSONObject obj = new JSONObject();

            try {
                obj.put("answerid", answerid);
                obj.put("answer", answer);
                obj.put("question", question);
                obj.put("choice1", choice1);
                obj.put("choice2", choice2);
                obj.put("choice3", choice3);

                topobj.put(objName, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        musea.setQuiz(quizz);
        Log.e("Museum", "Value is: " + values);
    }

    public Musea(ArrayList<String> musea, Object quiz) {
        this.musea = musea;
        this.quiz = quiz;
    }

    public ArrayList<String> getMusea() {
        return musea;
    }

    public void setMusea(ArrayList<String> musea) {
        this.musea = musea;
    }

    public Object getQuiz() {
        return quiz;
    }

    public void setQuiz(Object quiz) {
        this.quiz = quiz;
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("author", author);
//        result.put("body", body);
//        result.put("time", time);
//
//        return result;
//    }
}
