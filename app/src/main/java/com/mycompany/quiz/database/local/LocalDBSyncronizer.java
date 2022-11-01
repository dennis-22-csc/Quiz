package com.mycompany.quiz.database.local;

import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;

import com.mycompany.quiz.domain.Category;
import com.mycompany.quiz.domain.EssayQuestion;
import com.mycompany.quiz.domain.Question;

public class LocalDBSyncronizer {

    LocalDBHelper localDBHelper;
	
    public LocalDBSyncronizer(Context context){
		localDBHelper=new LocalDBHelper(context);
	}

   public void saveCategories (JSONArray categories,int selectedClass) throws JSONException {
	 List<Category> categoryList = new ArrayList<>();
	 categoryList.clear();

	 for (int i = 0; i < categories.length(); i++) {
	 JSONObject obj = categories.getJSONObject(i);

	 categoryList.add(new Category(
	 obj.getString("categoryName")
	 ));
	 }

	 localDBHelper.addCategories(categoryList, selectedClass);

	 }

	public void saveQuestions (JSONArray questions, int selectedClass) throws JSONException { 

		List<Question> questionList = new ArrayList<>();
		questionList.clear();

		for (int i = 0; i < questions.length(); i++) {
			JSONObject obj = questions.getJSONObject(i);

			questionList.add(new Question(
								 obj.getString("question"),
								 obj.getString("option1"),
								 obj.getString("option2"),
								 obj.getString("option3"),
								 obj.getString("option4"),
								 obj.getString("correctOption"),
								 obj.getInt("categoryID")
							 ));
		}

		localDBHelper.addMCQuestions(questionList, selectedClass);

	} 

	public void saveEssayQuestions (JSONArray questions, int selectedClass) throws JSONException { 

		List<EssayQuestion> questionListEssay = new ArrayList<>();
		questionListEssay.clear();

		for (int i = 0; i < questions.length(); i++) {
			JSONObject obj = questions.getJSONObject(i);

			questionListEssay.add(new EssayQuestion(obj.getString("question"), obj.getString("answer"), obj.getInt("categoryID")));
		}

		localDBHelper.addEssayQuestions(questionListEssay, selectedClass);

		
	} 
	
    
}
