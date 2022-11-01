package com.mycompany.quiz.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.mycompany.quiz.quiz.QuizController;


public class CallDetector extends BroadcastReceiver {
    String state;

    @Override
    public void onReceive(Context context, Intent intent) {
        QuizController quizController=new QuizController(context);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            state = extras.getString(TelephonyManager.EXTRA_STATE);


            boolean quizShown=quizController.getQuizShownBoolean();
            boolean activate =quizController.getQuizActivatedBoolean();
            boolean quizPause=quizController.getQuizPausePreference();

            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)&&activate) {
                //callOngoing or on hold or you dialed your phone

                if (quizShown) {
                    context.sendBroadcast(new Intent("CALL"));
                } else {
                    quizController.cancelQuiz();
                    quizController.storeTimeRemaining();
                    //String cl = "Call Ongoing Quiz Deactivated";
                    //Toast.makeText(context, cl, Toast.LENGTH_LONG).show();
                }

            }

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)&&activate&&!quizPause) {
                //callEnded

                if (quizShown) {
                    quizController.rescheduleQuiz(20000);
                    //Toast.makeText(context,"Call ended, Call detector has rescheduled shown quiz for next ten seconds",Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(context, "Call Ended Quiz Activated", Toast.LENGTH_LONG).show();
                    long timeRemaining = quizController.getTimeRemaining();

                    long screenOffTime = quizController.getScreenOffTime();
                    long screenOffInterval = System.currentTimeMillis() - screenOffTime;
                    long timeToSchedule = timeRemaining - screenOffInterval;
                    //String durationText = DateUtils.formatElapsedTime(timeToSchedule / 1000);


                    if (screenOffInterval < timeRemaining) {
                        quizController.rescheduleQuiz(timeToSchedule);
                        //Toast.makeText(context, "Call ended, Call detector has rescheduled quiz for next " + durationText + ".", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(context, "Call ended, Call detected has rescheduled quiz for next 20 seconds", Toast.LENGTH_LONG).show();
                        quizController.rescheduleQuiz(20000);
                    }

                }

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)&&activate) {
                //phoneRinging

                if (quizShown) {
                    context.sendBroadcast(new Intent("CALL"));
                } else {
                    //Toast.makeText(context, "Phone Ringing Quiz Deactivated", Toast.LENGTH_LONG).show();
                    quizController.cancelQuiz();
                    quizController.storeTimeRemaining();
                }
            }


        }

    }

    //Toast.makeText(context, "Call detected", Toast.LENGTH_LONG).show();

}