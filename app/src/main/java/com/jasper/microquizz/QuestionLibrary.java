package com.jasper.microquizz;

public class QuestionLibrary {

    private String mQuestions [] = {
            "Wanneer was Vincent van Gogh geboren?",
            "Hoe heet het beroemde schilderij van Vincent van Gogh?",
            "Welk lichaamsdeel is Vincent van Gogh verloren?",
            "Wanneer werd was beroemde schilderij van Vincent van Gogh geschilderd?"

    };


    private String mChoices [][] = {
            {"30 maart 1853", "28 februari 1900", "2 maart 1920", "1 april 1970"},
            {"De banaan", "De tulp", "Zonnebloemen", "De nachtwacht"},
            {"Been", "Vinger", "Teen", "Oor"},
            {"1920", "1880", "1889", "1885"}
    };



    private String mCorrectAnswers[] = {"30 maart 1853", "Zonnebloemen", "Oor", "1889"};




    public String getQuestion(int a) {
        String question = mQuestions[a];
        return question;
    }


    public String getChoice1(int a) {
        String choice0 = mChoices[a][0];
        return choice0;
    }


    public String getChoice2(int a) {
        String choice1 = mChoices[a][1];
        return choice1;
    }

    public String getChoice3(int a) {
        String choice2 = mChoices[a][2];
        return choice2;
    }

    public String getChoice4(int a) {
        String choice3 = mChoices[a][3];
        return choice3;
    }

    public String getCorrectAnswer(int a) {
        String answer = mCorrectAnswers[a];
        return answer;
    }

}
