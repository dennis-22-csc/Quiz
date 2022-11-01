package com.mycompany.quiz.utilities;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.text.format.DateUtils;
import android.view.accessibility.AccessibilityEvent;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import com.mycompany.quiz.quiz.QuizController;
import com.mycompany.quiz.quiz.QuizReceiver;

public class BackgroundQuizPopupService extends AccessibilityService {

    Util util;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;


        config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);

        util=new Util(getApplicationContext());

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        try {

            QuizController quizController=new QuizController(getApplicationContext());
            boolean activate=quizController.getQuizActivatedBoolean();
            boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 1,
                    new Intent(getApplicationContext(), QuizReceiver.class),
                    PendingIntent.FLAG_NO_CREATE) != null);
            boolean pauseNotificationIntentUp = (PendingIntent.getBroadcast(getApplicationContext(), 101,
                    new Intent(getApplicationContext(), QuizReactivator.class),
                    PendingIntent.FLAG_NO_CREATE) != null);

            boolean quizShown=quizController.getQuizShownBoolean();
            boolean quizPause=quizController.getQuizPausePreference();
            boolean notificationIsVisible=util.isNotificationVisible();
            int batLevel=util.checkBatteryLevel();
            boolean isScreenOn=util.isInteractive();

            if(activate&&batLevel>10&&isScreenOn&&!alarmUp&&!quizPause) {
                long timeRemaining=quizController.getTimeRemaining();

                quizController.storeTimeRemaining();

                long screenOffTime=quizController.getScreenOffTime();
                long screenOffInterval=System.currentTimeMillis()-screenOffTime;
                long timeToSchedule=timeRemaining-screenOffInterval;

                String durationText = util.getTime(timeToSchedule);

                if (screenOffInterval<timeRemaining) {
                    quizController.rescheduleQuiz(timeToSchedule);
                    Toast.makeText(getApplicationContext(),"Accessibility has rescheduled quiz for next " + durationText, Toast.LENGTH_LONG).show();
                } else {
                    quizController.rescheduleQuiz(20000);
                    Toast.makeText(getApplicationContext(),"Accessibility has rescheduled quiz for 20 seconds", Toast.LENGTH_LONG).show();
                }

                //Toast.makeText(getApplicationContext(),"Accessibility has rescheduled quiz for " +timeToSchedule + ".", Toast.LENGTH_LONG).show();

            } else if (activate&&alarmUp&&batLevel<=10) {
                util.cancelNotification(0);
                quizController.cancelQuiz();
                quizController.storeTimeRemaining();

                //Toast.makeText(getApplicationContext(),"Low battery, scheduled quiz cancelled.",Toast.LENGTH_LONG).show();

            } else if (activate&&alarmUp&&!isScreenOn) {
                util.cancelNotification(0);
                quizController.cancelQuiz();
                quizController.storeTimeRemaining();
            }

            if (quizShown&&!notificationIsVisible) {
                util.showQuizNotification();
            }

            if (activate&&quizPause&&!pauseNotificationIntentUp){
                quizController.schedulePauseNotification();
                //Toast.makeText(getApplicationContext(), "Hey", Toast.LENGTH_LONG).show();
            }



        } catch (Throwable e) {
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    //}

    @Override
    public void onInterrupt() {

    }




}
