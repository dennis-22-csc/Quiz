package com.mycompany.quiz.database.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mycompany.quiz.domain.EssayQuestion;
import com.mycompany.quiz.domain.Question;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefHelper {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;
    Set<String> questionsSet;

    public SharedPrefHelper(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("quiz", 0);
        editor = prefs.edit();
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public boolean getSyncQuestionBoolean(int selectedClass) {
        int prefSelectedClass = prefs.getInt("selected_class_question", 0);
        if (selectedClass == prefSelectedClass) {
            return prefs.getBoolean("sync_question", false);
        }
        return false;
    }

    public boolean getSyncCategoryBoolean(int selectedClass) {
        int prefSelectedClass = prefs.getInt("selected_class_category", 0);
        if (selectedClass == prefSelectedClass) {
            return prefs.getBoolean("sync_category", false);
        }
        return false;
    }

    public int getSelectedCategory() {
        return prefs.getInt("selectedCategory", 0);
    }

    public int getSelectedClass() {
        return prefs.getInt("selectedClass", 0);
    }

    public int getSelectedQuestionType() {
        return prefs.getInt("selectedQuestionType", 0);
    }

    public boolean getQuizActivatedBoolean() {
        return prefs.getBoolean("activate", false);
    }

    public boolean getQuizShownBoolean() {
        return prefs.getBoolean("QuizShown", false);
    }
    public boolean getFirstQuizShownBoolean(){return prefs.getBoolean("FirstQuizShown", false);
    }
    public void removeQuizShownBoolean() {
        editor.remove("QuizShown").commit();
    }

    public void clearPreferences() {
        editor.remove("activate").remove("selectedClass").remove("question_index_set").remove("hasShownEssayQuestion").remove("QuizShown").remove("FirstQuizShown").commit();
    }

    public void saveActivateQuizPreferences(int selectedQuestionType, int selectedCategory, int selectedClass) {
        editor.putInt("selectedQuestionType", selectedQuestionType).apply();
        editor.putInt("selectedCategory", selectedCategory).apply();
        editor.putInt("selectedClass", selectedClass).apply();
        editor.putBoolean("activate", true).apply();
    }

    public boolean isSetExist() {
        return prefs.contains("question_index_set");
    }

    public Set<String> getQuestionIndexSet() {

        return prefs.getStringSet("question_index_set", new HashSet<String>());

    }

    public void removeQuestionIndexSet() {
        //TODO: Add key to constant
        //TODO: Create a showToast method
        editor.remove("question_index_set").apply();
    }

    public void saveQuestionIndexSet(List<String> questionIdList) {
        questionsSet = new HashSet<String>(questionIdList);

        editor.putStringSet("question_index_set", questionsSet).apply();
    }

    public void saveQuizShownBoolean(boolean value) {
        editor.putBoolean("QuizShown", value);
        editor.commit();

    }

    public void setMucQuestionShownBoolean() {
        editor.putBoolean("hasShownEssayQuestion", false);
        editor.commit();
    }

    public void setPauseQuizPreference(boolean value) {
        editor.putBoolean("PauseQuiz", value);
        editor.commit();
    }



    public boolean hasShownEssayQuestion() {
        return prefs.getBoolean("hasShownEssayQuestion", false);
    }

    public void setEssayQuestionShownBoolean() {
        editor.putBoolean("hasShownEssayQuestion", true);
        editor.commit();
    }

    public void setSyncCategoryPreferences(int selectedClass, boolean value) {
        editor.putInt("selected_class_category", selectedClass).apply();
        editor.putBoolean("sync_category", value).apply();
    }

    public void setSyncQuestionPreferences(int selectedClass, boolean value) {
        editor.putInt("selected_class_question", selectedClass).apply();
        editor.putBoolean("sync_question", value).apply();
    }


    public void clearCategorySyncPreferences() {
        editor.putInt("selected_class_category", 0).apply();
        editor.putBoolean("sync_category", false).apply();
    }

    public void clearQuestionSyncPreferences() {
        editor.putInt("selected_class_question", 0).apply();
        editor.putBoolean("sync_question", false).apply();
    }

    public void storeQuestionTriggerTime(long questionTriggerTime) {
        editor.putLong("QuestionTriggerTime", questionTriggerTime).apply();
    }

    public long getQuestionTriggerTime() {
        return prefs.getLong("QuestionTriggerTime", 0);
    }

    public void storeTimeRemaining(long timeRemaining, long screenOffTime) {
        editor.putLong("TimeRemaining", timeRemaining);
        editor.putLong("ScreenOffTime", screenOffTime);
        editor.commit();
    }

    public long getTimeRemaining() {
        return prefs.getLong("TimeRemaining", 0);
    }

    public long getScreenOffTime() {
        return prefs.getLong("ScreenOffTime", 0);
    }

    public boolean getPauseQuizPreference() {
        return prefs.getBoolean("PauseQuiz", false);
    }

    public void saveCurrentMucQuestion(Question currentQuestion){
        editor.putString("CurrentMucQuestion", new Gson().toJson(currentQuestion)).apply();
    }

    public Question getCurrentMucQuestion(){
        String currentQuestionString=prefs.getString("CurrentMucQuestion", "");
        return new Gson().fromJson(currentQuestionString, Question.class);
    }

    public void saveCurrentEssayQuestion(EssayQuestion currentQuestion){
        editor.putString("CurrentEssayQuestion", new Gson().toJson(currentQuestion)).apply();
    }

    public EssayQuestion getCurrentEssayQuestion(){
        String currentQuestionString=prefs.getString("CurrentEssayQuestion", "");
        return new Gson().fromJson(currentQuestionString, EssayQuestion.class);
    }


}
