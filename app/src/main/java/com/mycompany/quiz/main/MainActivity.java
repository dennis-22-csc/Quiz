package com.mycompany.quiz.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.View;
import android.view.MenuItem;
import android.widget.PopupMenu;

import java.util.List;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mycompany.quiz.R;
import com.mycompany.quiz.database.local.LocalDBHelper;
import com.mycompany.quiz.database.local.SharedPrefHelper;
import com.mycompany.quiz.domain.Category;
import com.mycompany.quiz.quiz.QuizController;
import com.mycompany.quiz.quiz.QuizReceiver;
import com.mycompany.quiz.sync.SyncCategoryActivity;
import com.mycompany.quiz.sync.SyncQuestionActivity;
import com.mycompany.quiz.utilities.Util;

public class MainActivity extends AppCompatActivity {
	QuizController quizController;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		quizController =new QuizController(this);

	}

	public void activateQuiz(View view) {
		displaySelectClassPopupMenu(view);
	}
    public void checkQuizStatus(View view){
    	showQuizStatus();
	}
	public void deactivateQuiz(View view){
		boolean isActivated=quizController.getQuizActivatedBoolean();
		if(isActivated){
			quizController.deactivateQuiz();
			showToast("Quiz Deactivated");
		} else {
			showToast("Quiz is not activated");
		}
	}

    public void pauseQuiz(View view){
            quizController.pauseQuiz();
            showToast("Quiz Paused");
    }

    public void syncCategory(View view) {

        PopupMenu popup = new PopupMenu(this, view);

        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.pop_class, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.java:
                        startCategorySyncActivity(1);
                        return true;
					case R.id.c:
						startCategorySyncActivity(2);
						return true;
					case R.id.linux:
                        startCategorySyncActivity(3);
                        return true;
                    case R.id.general:
                        startCategorySyncActivity(4);
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();

    }

    public void syncQuestion (View view) {
        PopupMenu popup = new PopupMenu(this, view);

        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.pop_class, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.java:
                        startQuestionSyncActivity(1);
                        return true;
					case R.id.c:
						startQuestionSyncActivity(2);
						return true;
					case R.id.linux:
                        startQuestionSyncActivity(3);
                        return true;
                    case R.id.general:
                        startQuestionSyncActivity(4);
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    private void displaySelectClassPopupMenu(View view) {
		
    PopupMenu popup = new PopupMenu(this, view);
    
	// Inflate the menu from xml
	popup.getMenuInflater().inflate(R.menu.pop_class, popup.getMenu());
	// Setup menu item selection
	popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
	public boolean onMenuItemClick(MenuItem item) {
		boolean question_sync=false;

		switch (item.getItemId()) {
			case R.id.java:
				question_sync=quizController.getQuestionSyncBoolean(1);
				if(question_sync){
					displayCategoryPopupMenu(view, 1);
				} else {showToast("You have a pending Java questions sync");}
				return true;
			case R.id.c:
				question_sync=quizController.getQuestionSyncBoolean(2);
				if(question_sync){
					displayCategoryPopupMenu(view, 2);
				} else {showToast("You have a pending C questions sync");}
				return true;
			case R.id.linux:
				question_sync=quizController.getQuestionSyncBoolean(3);
				if(question_sync) {
					displayCategoryPopupMenu(view, 3);
				} else {showToast("You have a pending Linux questions sync");}
				return true;
			case R.id.general:
				question_sync=quizController.getQuestionSyncBoolean(4);
				if(question_sync) {
					displayCategoryPopupMenu(view, 4);
				} else {showToast("You have a pending General questions sync");}
				return true;
			default:
				return false;
		}
		}
	});
	// Handle dismissal with: popup.setOnDismissListener(...);
	// Show the menu
	popup.show(); 

	}

	private void displayCategoryPopupMenu(final View view, final int selectedClass) {
		PopupMenu popupMenu=new PopupMenu(this, view);

		popupMenu.getMenu().add(Menu.NONE, 0, 0, "All");
		
		List<Category> categoryList=quizController.getCategoriesFromLocalDB(selectedClass);

		for(Category category:categoryList) {
			int count=category.getId();
			popupMenu.getMenu().add(Menu.NONE, count, count, category.getName());
		}

		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int selectedCategory=item.getItemId();
				displayQuestionTypePopupMenu(view, selectedClass, selectedCategory);
				return true;
			}
		});

		popupMenu.show();
	}


	private void displayQuestionTypePopupMenu(final View view, final int selectedClass, final int selectedCategory) {
		PopupMenu popupMenu=new PopupMenu(MainActivity.this, view);

		//popupMenu.getMenu().add(Menu.NONE, 1, 1, "Multiple Choice");
		popupMenu.getMenu().add(Menu.NONE, 2, 2, "Essay");
		// popupMenu.getMenu().add(Menu.NONE, 3, 3, "Both");


		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int selectedQuestionType=item.getItemId();
				quizController.saveActivateQuizPreferences(selectedQuestionType, selectedCategory, selectedClass);
				quizController.scheduleFirstQuiz();
				showToast("Quiz activated");
				return true;
			}
		});

		popupMenu.show();
	}

	private void startCategorySyncActivity(int selectedClass){
		boolean isOnline=quizController.isOnline();

		if (isOnline) {
			Intent intent = new Intent(MainActivity.this, SyncCategoryActivity.class);
			intent.putExtra("selectedClass",selectedClass);
			startActivity(intent);
		}
		else {
			showToast("You are yet to turn on internet connection!");
		}
	}

	private void startQuestionSyncActivity(int selectedClass){
		boolean sync_category=quizController.getCategorySyncBoolean(selectedClass);
		boolean isOnline=quizController.isOnline();

		if (sync_category&&isOnline) {
			Intent intent = new Intent(MainActivity.this, SyncQuestionActivity.class);
			intent.putExtra("selectedClass",selectedClass);
			startActivity(intent);
		} else if (!sync_category) {
		    showToast("There is a need for you to sync categories!");
		} else if (!isOnline) {
			showToast("You are yet to turn on internet connection!");
		}
	}

	private void testDbCodes() {

		LocalDBHelper localDBHelper=new LocalDBHelper(this);
		List<Category> myList=localDBHelper.getAllCategories(1);

		showToast(String.valueOf(myList.size()));

	}

	private void showToast(String text){
    	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public void showQuizStatus(){
		boolean isActivated=quizController.getQuizActivatedBoolean();
		boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 1,
				new Intent(getApplicationContext(), QuizReceiver.class),
				PendingIntent.FLAG_NO_CREATE) != null);

		if (isActivated) {
			if (alarmUp) {
				showToast("Quiz is scheduled");
			} else {
				showToast("Quiz is not scheduled");
			}
		} else {
			showToast("Quiz is not yet activated");
		}
	}
	
}
