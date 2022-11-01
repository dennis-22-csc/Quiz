package com.mycompany.quiz.quiz;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import android.app.AlertDialog;
import android.view.LayoutInflater;

import android.util.DisplayMetrics;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import androidx.appcompat.app.AppCompatActivity;

import com.mycompany.quiz.R;
import com.mycompany.quiz.database.local.SharedPrefHelper;
import com.mycompany.quiz.utilities.Util;


public class QuizActivity extends AppCompatActivity {

    int selectedCategory, selectedClass;

    AlertDialog.Builder mAlertDlgBuilder;
    AlertDialog mAlertDialog;
    View mDialogView;

    BroadcastReceiver callBroadcastReceiver;
    BroadcastReceiver rescheduleBroadcastReceiver;

    SharedPrefHelper sharedPrefHelper;
    Util util;
    QuizController quizController;

    MucQuizActivityHelper mucQuizActivityHelper;
    EssayQuizActivityHelper essayQuizActivityHelper;

    @Override
    protected void onResume() {
        super.onResume();
        sharedPrefHelper.saveQuizShownBoolean(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callBroadcastReceiver);
        unregisterReceiver(rescheduleBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        try {
            initDependencies();
            showNotification();
            showAudio();

            registerCallBroadcastReceiver();
            registerRescheduleBroadcastReceiver();

            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                int selectedCategory = extras.getInt("selectedCategory");
                int selectedQuestionType = extras.getInt("selectedQuestionType");
                int selectedClass = extras.getInt("selectedClass");

                showHomeDialog(selectedClass, selectedCategory, selectedQuestionType);

            }
        } catch (Throwable e) {
            showToast(e.getMessage());
        }


    }

    public void endQuiz(View view) {
        finishAndRemoveTask();
    }

    private void initDependencies() {
        util = new Util(this);
        quizController=new QuizController(this);
        sharedPrefHelper=new SharedPrefHelper(this);
    }

    private void registerCallBroadcastReceiver() {
        callBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finishAndRemoveTask();
            }
        };

        registerReceiver(callBroadcastReceiver, new IntentFilter("CALL"));

    }

    private void registerRescheduleBroadcastReceiver() {
        rescheduleBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finishAndRemoveTask();
            }
        };

        registerReceiver(rescheduleBroadcastReceiver, new IntentFilter("RESCHEDULE"));

    }






    private void showHomeDialog(int selectedClass, int selectedCategory, int selectedQuestionType) {

        this.selectedClass = selectedClass;
        this.selectedCategory = selectedCategory;

        buildDialog();

        mucQuizActivityHelper =new MucQuizActivityHelper(this, mDialogView, selectedClass, selectedCategory);
        essayQuizActivityHelper =new EssayQuizActivityHelper(this, mDialogView, mucQuizActivityHelper, selectedClass, selectedCategory);
        switch (selectedQuestionType) {
            case 1:
                mucQuizActivityHelper.initMucViews();
                mucQuizActivityHelper.showMCQuestion();
                break;
            case 2:
                mucQuizActivityHelper.initMucViews();
                essayQuizActivityHelper.initEssayViews();
                essayQuizActivityHelper.showEssayQuestion();
                break;

            case 3:
                if (sharedPrefHelper.hasShownEssayQuestion()) {
                    mucQuizActivityHelper.initMucViews();
                    mucQuizActivityHelper.showMCQuestion();
                } else {
                    mucQuizActivityHelper.initMucViews();
                    essayQuizActivityHelper.initEssayViews();
                    essayQuizActivityHelper.showEssayQuestion();
                }

                break;
        }


    }

    private void buildDialog() {

        LayoutInflater inflater = getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.activity_quiz, null);

        // Build the dialog
        mAlertDlgBuilder = new AlertDialog.Builder(this);
        mAlertDlgBuilder.setCancelable(false);
        mAlertDlgBuilder.setView(mDialogView);
        mAlertDialog = mAlertDlgBuilder.create();
        mAlertDialog.show();

        DisplayMetrics metrics = new DisplayMetrics(); //get metrics of screen
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = (int) (metrics.heightPixels * 0.9); //set height to 90% of total
        int width = (int) (metrics.widthPixels * 0.9); //set width to 90% of total
        mAlertDialog.getWindow().setLayout(width, height); //set layout

    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void showNotification() {
        util.showQuizNotification();
    }

    private void showAudio() {
        try {
            util.soundAudio();
        } catch (IOException e) {
            showToast(e.getMessage());
        }
    }

    public void cancelNotification() {
        util.cancelNotification(0);
    }

}

