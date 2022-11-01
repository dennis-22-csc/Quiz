package com.mycompany.quiz.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mycompany.quiz.R;
import com.mycompany.quiz.quiz.QuizController;


public class SyncQuestionActivity extends AppCompatActivity {
	QuizController controller;
	RemoteLocalSyncReceiver remoteLocalSyncReceiver;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int selectedClass = extras.getInt("selectedClass");
			controller=new QuizController(this);
			controller.addQuestionsToLocalDB(selectedClass,2);
			registerRemoteLocalSyncReceiver();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		unregisterRemoteLocalSyncReceiver();
	}


	private void registerRemoteLocalSyncReceiver() {
		remoteLocalSyncReceiver = new RemoteLocalSyncReceiver();
		registerReceiver(remoteLocalSyncReceiver, new IntentFilter("REMOTE_HOST_RESPONSE"));
	}

	private void unregisterRemoteLocalSyncReceiver(){
		if(remoteLocalSyncReceiver!=null) {
			unregisterReceiver(remoteLocalSyncReceiver);//<-- Unregister to avoid memoryleak
		}
	}


	private class RemoteLocalSyncReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent.getAction().equals("REMOTE_HOST_RESPONSE")) {
				String response = intent.getStringExtra("sync_status");
				int selectedClass=intent.getIntExtra("selectedClass", 0);

				switch(response) {
					case "muc_question_synced":
						showToast("Muc synced. Now syncing essay");

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								controller.addQuestionsToLocalDB(selectedClass, 2);
							}
						}, 2000);

						break;
					case "essay_question_synced":
						showToast("Essay questions synced successfully");
						controller.saveQuestionSyncPreferences(selectedClass, true);
						controller.clearCategorySyncPreferences();
						finish();
						break;
					case "error_muc_question":
						showToast("Unable to sync multiple choice questions");
						controller.saveQuestionSyncPreferences(selectedClass, false);
						finish();
					case "error_essay_question":
						showToast("Unable to sync essay questions");
						controller.saveQuestionSyncPreferences(selectedClass, false);
						finish();
						break;
					default:
						setContentView(R.layout.fragment_message);
						printText(response);
				}

			}
		}
	}

	private void printText(String text) {
		TextView textView=findViewById(R.id.tv);
		textView.append(text);
	}

	private void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}


}
