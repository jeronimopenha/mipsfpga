/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufv_mipsfpga;

import java.util.Vector;

/**
 *
 * @author geraldo
 */
public class question {

    private String question;
    private String answer_1;
    private String answer_2;
    private String answer_3;
    private String answer_4;
    private String answer_5;
    private int questionNumber;
    private Vector<String> correctAnswer;

    public question() {
    }

    //-------------------------------   get and set ---------------------------------------------------
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer_1() {
        return answer_1;
    }

    public void setAnswer_1(String answer_1) {
        this.answer_1 = answer_1;
    }

    public String getAnswer_2() {
        return answer_2;
    }

    public void setAnswer_2(String answer_2) {
        this.answer_2 = answer_2;
    }

    public String getAnswer_3() {
        return answer_3;
    }

    public void setAnswer_3(String answer_3) {
        this.answer_3 = answer_3;
    }

    public String getAnswer_4() {
        return answer_4;
    }

    public void setAnswer_4(String answer_4) {
        this.answer_4 = answer_4;
    }

    public String getAnswer_5() {
        return answer_5;
    }

    public void setAnswer_5(String answer_5) {
        this.answer_5 = answer_5;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Vector<String> getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Vector<String> correctAnswer) {
        //-------------------- get and set--------------------------------------------------------------------

        this.correctAnswer = correctAnswer;
    }

    public void print() {
        System.out.println(this.getQuestion() + "\n" + this.getAnswer_1() + " :" + this.getCorrectAnswer().get(0) + "\n" + this.getAnswer_2() + " :" + this.getCorrectAnswer().get(1) + "\n" + this.getAnswer_3() + " :" + this.getCorrectAnswer().get(2) + "\n" + this.getAnswer_4() + " :" + this.getCorrectAnswer().get(3) + "\n" + this.getAnswer_5() + " :" + this.getCorrectAnswer().get(4) + "\n");
    }

}
