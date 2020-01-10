package com.jasper.microquizz.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Museums {
    private int currentMuseum;
    private int currentObject;
    private int currentquiz;

    private ArrayList<String> museaList;
    private ArrayList quizlist;
    private ArrayList objectList;

    public Museums() {
        this.currentMuseum = 0;
        this.currentObject = 0;
        this.currentquiz = 0;
    }

    public HashMap<String, String> nextQuiz() {
        HashMap<String, String> result = new HashMap<>();
        currentObject++;
        if (currentObject < ((ArrayList) this.quizlist.get(currentMuseum)).size()) {
            result = (HashMap<String, String>) ((ArrayList) this.quizlist.get(currentMuseum)).get(currentObject);
        }
        if (result.size() == 0) {
            currentObject = 0;
        }
        return result;
    }

    public String getMuseaName() {
        return this.museaList.get(currentMuseum);
    }
    public String getMuseaName(int key) {
        return this.museaList.get(key);
    }

    public void setMuseaList(ArrayList<String> museaList) {
        this.museaList = museaList;
    }

    public String getObjectName() {
        return ((ArrayList) this.objectList.get(currentMuseum)).get(currentObject).toString();
    }
    public String getObjectName(int mKey, int oKey) {
        return ((ArrayList) this.objectList.get(mKey)).get(oKey).toString();
    }

    public void setObjectList(ArrayList objectList) {
        this.objectList = objectList;
    }

    public HashMap<String, String> getQuizName() {
        return (HashMap<String, String>) ((ArrayList) this.quizlist.get(currentMuseum)).get(currentObject);
    }
    public String getQuizName(int mKey, int oKey) {
        return ((ArrayList) this.quizlist.get(mKey)).get(oKey).toString();
    }

    public void setQuizlist(ArrayList quizlist) {
        this.quizlist = quizlist;
    }

    public int getCurrentMuseum() {
        return currentMuseum;
    }

    public void setCurrentMuseum(int currentMuseum) {
        this.currentMuseum = currentMuseum;
    }

    public int getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(int currentObject) {
        this.currentObject = currentObject;
    }

    public int getCurrentquiz() {
        return currentquiz;
    }

    public void setCurrentquiz(int currentquiz) {
        this.currentquiz = currentquiz;
    }
}
