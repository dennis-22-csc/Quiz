package com.mycompany.quiz.domain;

public class EssayQuestion { 

    private int id;
    private String question;
	private String answer;
	private int categoryID; 

	public EssayQuestion() {

	}
	public EssayQuestion(String question, String answer, int categoryID) {
		this.question=question;
		this.answer=answer;
		this.categoryID=categoryID;
	}
	public EssayQuestion(int id, String question, String answer, int categoryID) {
		this.id=id;
		this.question=question;
		this.answer=answer;
		this.categoryID=categoryID;
	}

	public void setId(int id) {
		this.id=id;
	}
	public int getId() {
		return id;
	}

	public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

	public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


	public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }



}
