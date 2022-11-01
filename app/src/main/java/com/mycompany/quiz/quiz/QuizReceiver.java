package com.mycompany.quiz.quiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.widget.Toast;

import com.mycompany.quiz.database.local.SharedPrefHelper;
import com.mycompany.quiz.utilities.Util;

import java.util.Calendar;

public class QuizReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        QuizController quizController=new QuizController(context);
        SharedPrefHelper sharedPrefHelper=new SharedPrefHelper(context);

        boolean activate =sharedPrefHelper.getQuizActivatedBoolean();
        boolean quizPause=quizController.getQuizPausePreference();
        //Toast.makeText(context,"Pause: " + quizPause, Toast.LENGTH_LONG).show();
        SharedPreferences prefs=sharedPrefHelper.getPrefs();

        if (!quizPause){
            quizController.scheduleOtherQuiz();
            //Toast.makeText(context,"Receiver has rescheduled quiz", Toast.LENGTH_LONG).show();
        }

            if (activate==true&&prefs.contains("selectedCategory")&&prefs.contains("selectedQuestionType")) {
            int selectedCategory=sharedPrefHelper.getSelectedCategory();
            int selectedQuestionType=sharedPrefHelper.getSelectedQuestionType();
            int selectedClass=sharedPrefHelper.getSelectedClass();

            Intent i = new Intent(context, QuizActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("selectedCategory",selectedCategory);
            i.putExtra("selectedQuestionType", selectedQuestionType);
            i.putExtra("selectedClass",selectedClass);
            context.startActivity(i);

        }


    }





}

