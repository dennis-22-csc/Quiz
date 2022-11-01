package com.mycompany.quiz.quiz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.mycompany.quiz.database.remote.RemoteDBHelper;
import com.mycompany.quiz.database.local.SharedPrefHelper;
import com.mycompany.quiz.utilities.QuizReactivator;
import com.mycompany.quiz.utilities.Util;
import com.mycompany.quiz.database.local.LocalDBHelper;
import com.mycompany.quiz.domain.Category;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class QuizController {
   RemoteDBHelper remoteDBHelper;
   LocalDBHelper localDBHelper;
   Util util;
   Context context;
   SharedPrefHelper sharedPrefHelper;
   
   public QuizController(Context context) {
       this.context=context;
      this.remoteDBHelper=new RemoteDBHelper(context);
	  this.localDBHelper=new LocalDBHelper(context);
	  this.util=new Util(context);
	  this.sharedPrefHelper=new SharedPrefHelper(context);
   } 
   
   public void addCategoriesToLocalDB(int selectedClass ) {
	   remoteDBHelper.addRemoteCategoriesToLocalDB(selectedClass);
   }
   
   public List<Category> getCategoriesFromLocalDB(int selectedClass) {
	  List<Category> categories=localDBHelper.getAllCategories(selectedClass);
	   
	   List<Category> subCategories=new ArrayList<>(); 

	   for (Category myCategories: categories) { 

		   Category myCategory=new Category(); 

		   if (!myCategories.getName().equals("Dummy")) { 
			   myCategory.setName(myCategories.getName()); 
			   myCategory.setId(myCategories.getId()); 

			   subCategories.add(myCategory);
		   }

	   } 
	   
	   return subCategories;
   }
   
   public void addQuestionsToLocalDB(int selectedClass, int selectedQuestionType){
	   remoteDBHelper.addRemoteQuestionsToLocalDB(selectedClass, selectedQuestionType);
   }

    public void saveActivateQuizPreferences(int selectedQuestionType, int selectedCategory, int selectedClass) {
        sharedPrefHelper.saveActivateQuizPreferences(selectedQuestionType, selectedCategory, selectedClass);
    }

    public void saveCategorySyncPreferences(int selectedClass, boolean value){
        sharedPrefHelper.setSyncCategoryPreferences(selectedClass, value);
    }

    public void saveQuestionSyncPreferences(int selectedClass, boolean value){
        sharedPrefHelper.setSyncQuestionPreferences(selectedClass, value);
    }

    public void clearCategorySyncPreferences(){
       sharedPrefHelper.clearCategorySyncPreferences();
    }

    public void clearQuestionSyncPreferences(){
        sharedPrefHelper.clearQuestionSyncPreferences();
    }

    public void scheduleFirstQuiz() {

            Intent alarmIntent = new Intent(context, QuizReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar questionTime = Calendar.getInstance();
            questionTime.add(Calendar.SECOND, 5);
            long questionTriggerTime=questionTime.getTimeInMillis();

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, questionTriggerTime, pendingIntent);

            storeQuestionTriggerTime(getDefaultQuestionTriggerTime());
    }

    public void scheduleOtherQuiz() {

        Intent alarmIntent = new Intent(context, QuizReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long questionTriggerTime=getDefaultQuestionTriggerTime()+System.currentTimeMillis();

        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, questionTriggerTime, pendingIntent);

        storeQuestionTriggerTime(questionTriggerTime);

    }


    public void rescheduleQuiz(long questionTriggerTime) {

            Intent alarmIntent = new Intent(context, QuizReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,questionTriggerTime+System.currentTimeMillis(), pendingIntent);

            storeQuestionTriggerTime(questionTriggerTime+System.currentTimeMillis());

    }


    public void cancelQuiz() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel Alarm using Reminder ID
        Intent intent2 = new Intent(context, QuizReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent2, 0);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private void cancelPauseAlarm() {
        Intent intent = new Intent(context, QuizReactivator.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0);
        pendingIntent.cancel();

        sharedPrefHelper.setPauseQuizPreference(false);
    }




    /*public long getQuestionCount(int selectedClass, int selectedCategory, int selectedQuestionType){
	   return localDBHelper.countQuestions(selectedClass,selectedCategory, selectedQuestionType);
   }*/

   public boolean getQuizActivatedBoolean(){
       return sharedPrefHelper.getQuizActivatedBoolean();
   }
   public boolean getQuizShownBoolean(){
       return sharedPrefHelper.getQuizShownBoolean();
   }
   public boolean getCategorySyncBoolean(int selectedClass) {return sharedPrefHelper.getSyncCategoryBoolean(selectedClass);}
   public boolean getQuestionSyncBoolean(int selectedClass) {
       return sharedPrefHelper.getSyncQuestionBoolean(selectedClass);
   }

   public long getTimeRemaining(){
       return sharedPrefHelper.getTimeRemaining();
   }
   public long getScreenOffTime(){
       return sharedPrefHelper.getScreenOffTime();
   }
   public long getQuestionTriggerTime(){
       return sharedPrefHelper.getQuestionTriggerTime();
   }

   public long getDefaultQuestionTriggerTime(){
       long questionTime= TimeUnit.MINUTES.toMillis(10);
       return questionTime;
   }

   public void storeTimeRemaining(){
           long questionTriggerTime=getDefaultQuestionTriggerTime();
           long timeRemaining= questionTriggerTime-System.currentTimeMillis();
           long screenOffTime=System.currentTimeMillis();
           sharedPrefHelper.storeTimeRemaining(timeRemaining, screenOffTime);
   }

   public void storeQuestionTriggerTime(long questionTriggerTime){
       sharedPrefHelper.storeQuestionTriggerTime(questionTriggerTime);
   }

    public void schedulePauseNotification() {

        Intent alarmIntent = new Intent(context, QuizReactivator.class);
        alarmIntent.putExtra("action", "notify");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.add(Calendar.HOUR, 1);
        long notificationTriggerTime=notificationTime.getTimeInMillis();

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTriggerTime, pendingIntent);

    }

    public void pauseQuiz(){
       cancelQuiz();
       sharedPrefHelper.setPauseQuizPreference(true);
   }
   public boolean getQuizPausePreference(){
       return sharedPrefHelper.getPauseQuizPreference();
   }
   public  void resumeQuiz(int id){
       util.cancelNotification(id);
       cancelPauseAlarm();
   }

   public void deactivateQuiz(){
       sharedPrefHelper.clearPreferences();
       util.cancelNotification(0);
       cancelQuiz();
       cancelPauseAlarm();
   }

   public boolean isOnline(){
       return util.isNetworkAvailable();
   }
	
}
