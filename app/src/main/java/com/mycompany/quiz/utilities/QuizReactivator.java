package com.mycompany.quiz.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mycompany.quiz.database.local.SharedPrefHelper;
import com.mycompany.quiz.quiz.QuizController;


public class QuizReactivator extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Util util=new Util(context);
        QuizController quizController=new QuizController(context);
        SharedPrefHelper sharedPrefHelper=new SharedPrefHelper(context);

        String action=intent.getStringExtra("action");
           if (action.equals("notify")){
               util.showPauseNotification();
           } else if (action.equals("pause")){
               util.cancelNotification(201);
               quizController.schedulePauseNotification();
               //Toast.makeText(context, "Pause", Toast.LENGTH_LONG).show();
           } else if (action.equals("resume")){
               quizController.resumeQuiz(201);
               //Toast.makeText(context, "Resume", Toast.LENGTH_LONG).show();
           } else if (action.equals("reschedule")){
               sharedPrefHelper.saveQuizShownBoolean(false);
               context.sendBroadcast(new Intent("RESCHEDULE"));
               util.cancelNotification(0);
               quizController.scheduleOtherQuiz();
        }
    }

}
