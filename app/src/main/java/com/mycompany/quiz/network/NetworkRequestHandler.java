package com.mycompany.quiz.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Intent;
import java.util.regex.Pattern;

import androidx.core.text.HtmlCompat;

import com.mycompany.quiz.database.local.LocalDBSyncronizer;

public class NetworkRequestHandler {
	
	NetworkRequestThread requestThread; 
	Context context;

	int selectedClass;
	int selectedCategory;
	int selectedQuestionType;
	int requestCode;
	String requestType;

	
	public NetworkRequestHandler(Context context, int selectedClass, int requestCode, String requestType) {
		this.context=context;
		this.selectedClass=selectedClass;
		this.requestCode=requestCode;
		this.requestType=requestType;
	}

	public NetworkRequestHandler(Context context, int selectedClass,int selectedQuestionType, int requestCode, String requestType) {
		this.context=context;
		this.selectedClass=selectedClass;
		this.selectedQuestionType=selectedQuestionType;
		this.requestCode=requestCode;
		this.requestType=requestType;
	}
	
	public void performCategoryNetworkRequest() {
		
		if (selectedClass==1) { 
			requestThread= new NetworkRequestThread(Api.URL_GET_JAVA_CATEGORIES);
		} else if (selectedClass==2) {
			requestThread= new NetworkRequestThread(Api.URL_GET_C_CATEGORIES);
		} else if (selectedClass==3) {
			requestThread= new NetworkRequestThread(Api.URL_GET_LINUX_CATEGORIES);
		} else if (selectedClass==4) {
			requestThread= new NetworkRequestThread(Api.URL_GET_CATEGORIES);
		}

		requestThread.execute();
	} 
	
	public void performQuestionNetworkRequest() {
		if (selectedClass==1&&selectedQuestionType==1) {
			requestThread = new NetworkRequestThread(Api.URL_GET_JAVA_QUESTIONS);
		} else if (selectedClass==1&&selectedQuestionType==2) {
			requestThread = new NetworkRequestThread(Api.URL_GET_JAVA_ESSAY_QUESTIONS);
		}

		if (selectedClass==2&&selectedQuestionType==1) {
			requestThread = new NetworkRequestThread(Api.URL_GET_C_QUESTIONS);
		} else if (selectedClass==2&&selectedQuestionType==2) {
			requestThread = new NetworkRequestThread(Api.URL_GET_C_ESSAY_QUESTIONS);
		}


		if (selectedClass==3&&selectedQuestionType==1) {
			requestThread = new NetworkRequestThread(Api.URL_GET_LINUX_QUESTIONS);
		} else if (selectedClass==3&&selectedQuestionType==2) {
			requestThread = new NetworkRequestThread(Api.URL_GET_LINUX_ESSAY_QUESTIONS);
		}

		if (selectedClass==4&&selectedQuestionType==1) {
			requestThread = new NetworkRequestThread(Api.URL_GET_QUESTIONS);
		} else if (selectedClass==4&&selectedQuestionType==2) {
		    requestThread = new NetworkRequestThread(Api.URL_GET_ESSAY_QUESTIONS);
	    }


		requestThread.execute();
		
	}
	
   
	private class NetworkRequestThread extends AsyncTask<Void, Void, String> {
		private String url;
		private static final int CODE_GET_REQUEST=1024;
		boolean isError;

		NetworkRequestThread(String url) {
			this.url = url;   
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}  

		@Override
		protected String doInBackground(Void... voids) {
			String response=null;
			try {
				if (requestCode == CODE_GET_REQUEST) {
					response=sendGetRequest(url);
				}
			} catch(Throwable e) {
				isError=true;
				response=e.getMessage();
			}

			return response;
		} 

		@Override
		protected void onPostExecute(String response) {
			super.onPostExecute(response); 

			boolean responseContainsHTMLTag=false; 

			//Determine if response contains html tags 
			if(response!=null&&!isError){
			    String regex ="<(?:\"[^\"]*\"['\"]*|'[^']*'['\"]*|[^'\">])+>";
			    responseContainsHTMLTag=Pattern.compile(regex).matcher(response).find();
            }

			if (response!=null&&!responseContainsHTMLTag&&!isError) {
				try {
					JSONObject object = new JSONObject(response);
					LocalDBSyncronizer dbSyncronizer=new LocalDBSyncronizer(context);

					switch (requestType) {
						case "question": 
							if (!object.getBoolean("error")) {
								if (selectedQuestionType == 1) {
									dbSyncronizer.saveQuestions(object.getJSONArray("quiz_q"), selectedClass);
									reportStatus("muc_question_synced");
								} else if (selectedQuestionType == 2) {
									dbSyncronizer.saveEssayQuestions(object.getJSONArray("quiz_q"), selectedClass);
									reportStatus("essay_question_synced");
								}

							} else {
								if (selectedQuestionType == 1) {
									reportStatus("error_muc_question");
								} else if (selectedQuestionType == 2) {
									reportStatus("error_essay_question");
								}
							}
							break; 
						case "category": 
							if (!object.getBoolean("error")) {
								dbSyncronizer.saveCategories(object.getJSONArray("quiz_c"), selectedClass);
								reportStatus("category_synced");
							} else {
								reportStatus("error_category");
							}

							break;
					}
				} catch(JSONException e) { 
					reportStatus(e.getMessage());
				}

		    } else if(responseContainsHTMLTag) {
				//removing HTML tags from response
				response= HtmlCompat.fromHtml(response, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();

				//displaying html response
				reportStatus(response);
			} else if(isError) {
				reportStatus(response);
			}
		}
		
	}
	
	
	private String sendGetRequest(String requestURL) throws Exception {
        StringBuilder sb = new StringBuilder();

            URL url = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }

        return sb.toString();
    }
	
	private void reportStatus(String response) {	
		Intent mIntent=new Intent();  
		mIntent.setAction("REMOTE_HOST_RESPONSE");
		mIntent.putExtra("sync_status", response);
		mIntent.putExtra("selectedClass", selectedClass);
		context.sendBroadcast(mIntent);
	} 
	
	/*private void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}*/

}
