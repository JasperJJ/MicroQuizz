package com.jasper.microquizz.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Museums {
    private int currentMuseumKey;
    private int currentObjectKey;
    private int currentquizKey;

    private ArrayList<String> museaList;
    private ArrayList quizlist;
    private ArrayList objectList;

    public Museums() {
        this.currentMuseumKey = 0;
        this.currentObjectKey = 0;
        this.currentquizKey = 0;
    }

    public HashMap<String, String> nextQuiz() {
        HashMap<String, String> result = new HashMap<>();
        currentquizKey++;
        if (currentquizKey < getQuizList().size()) {
            result = getQuiz(currentMuseumKey, currentObjectKey, currentquizKey);
        } else {
            currentquizKey = 0;
        }
        return result;
    }

	// Getters and Setter for the Musea

	public String getMuseaName() {
        return this.museaList.get(currentMuseumKey);
    }
    public String getMuseaName(int key) {
        return this.museaList.get(key);
    }

    public void setMuseaList(ArrayList<String> museaList) {
        this.museaList = museaList;
    }

	public ArrayList<String> getMuseaList() {
		return this.museaList;
	}

	// Getters and Setter for the Object

    public String getObjectName() {
        return ((ArrayList) this.objectList.get(currentMuseumKey)).get(currentObjectKey).toString();
    }
    public String getObjectName(int mKey, int oKey) {
        return ((ArrayList) this.objectList.get(mKey)).get(oKey).toString();
    }

    public void setObjectList(ArrayList objectList) {
        this.objectList = objectList;
    }

	public ArrayList<String> getObjectList() {
		return (ArrayList<String>) this.objectList.get(currentMuseumKey);
	}
	public ArrayList<String> getObjectList(int mKey) {
		return (ArrayList<String>) this.objectList.get(mKey);
	}

	// Getters and Setter for the Quiz

    public HashMap<String, String> getQuiz() {
        return (HashMap<String, String>) ((ArrayList) ((ArrayList) this.quizlist.get(currentMuseumKey)).get(currentObjectKey)).get(currentquizKey);
    }
    public HashMap<String, String> getQuiz(int mKey, int oKey, int qKey) {
        return ((HashMap) ((ArrayList) ((ArrayList) this.quizlist.get(mKey)).get(oKey)).get(qKey));
    }

    public void setQuizlist(ArrayList quizlist) {
        this.quizlist = quizlist;
    }

    public ArrayList<String> getQuizList() {
        return (ArrayList<String>) ((ArrayList) this.quizlist.get(currentMuseumKey)).get(currentObjectKey);
    }

    // Getters and Setters for the keys

    public int getCurrentMuseumKey() {
        return currentMuseumKey;
    }

    public void setCurrentMuseumKey(int currentMuseumKey) {
        this.currentMuseumKey = currentMuseumKey;
    }

    public int getCurrentObjectKey() {
        return currentObjectKey;
    }

    public void setCurrentObjectKey(int currentObjectKey) {
        this.currentObjectKey = currentObjectKey;
    }

    public int getCurrentquizKey() {
        return currentquizKey;
    }

    public void setCurrentquizKey(int currentquizKey) {
        this.currentquizKey = currentquizKey;
    }
}
