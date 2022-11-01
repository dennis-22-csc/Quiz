package com.mycompany.quiz.database.remote;

import android.content.Context;

import com.mycompany.quiz.network.NetworkRequestHandler;

public class RemoteDBHelper {

    private static final String REQUEST_TYPE_CATEGORY="category";
	private static final String REQUEST_TYPE_QUESTION="question";
	private static final int REQUEST_CODE_GET=1024;
	
	private Context context;
	
	public RemoteDBHelper(Context context) {
		this.context=context;
	}
	
    public void addRemoteCategoriesToLocalDB(int selectedClass) {
	    NetworkRequestHandler requestHandler=new NetworkRequestHandler(context, selectedClass, REQUEST_CODE_GET, REQUEST_TYPE_CATEGORY);
		requestHandler.performCategoryNetworkRequest();
	}
	
	public void addRemoteQuestionsToLocalDB(int selectedClass,int selectedQuestionType) {
	    NetworkRequestHandler requestHandler=new NetworkRequestHandler(context, selectedClass, selectedQuestionType, REQUEST_CODE_GET, REQUEST_TYPE_QUESTION);
		requestHandler.performQuestionNetworkRequest();
		//Toast.makeText(context,  "Selected Class: " + selectedClass + "Selected" +
				//"Question Type: " + selectedQuestionType, Toast.LENGTH_LONG).show();
	}
	
    
}
