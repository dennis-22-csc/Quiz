package com.mycompany.quiz.quiz;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mycompany.quiz.R;
import com.mycompany.quiz.database.local.LocalDBHelper;
import com.mycompany.quiz.database.local.SharedPrefHelper;
import com.mycompany.quiz.domain.Category;
import com.mycompany.quiz.domain.EssayQuestion;
import com.mycompany.quiz.utilities.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EssayQuizActivityHelper {
    List<Category> categoryList;

    TextView tv, textViewCategory, questionFeedback, hiddenText;

    EditText myEditText;

    List<EssayQuestion> essayQuestionList;
    EssayQuestion currentEssayQuestion;
    Category currentCategory;

    Button buttonGotIt, buttonFailedIt;

    RelativeLayout.LayoutParams buttonConfirmNextParams, textViewCategoryParams;

    Button buttonEndQuiz, buttonConfirmNext;

    int selectedClass;
    int selectedCategory;

    View mDialogView;
    QuizActivity quizActivity;
    SharedPrefHelper sharedPrefHelper;
    LocalDBHelper localDBHelper;
    MucQuizActivityHelper mucQuizActivityHelper;

    Set<String> questionIndexSet;
    List<String> questionIdList;

    boolean failedQuestion;
    int previousQuestionID;
    int questionCounter;
    String x;
    String myQuestion;
    String categoryName;


    public EssayQuizActivityHelper(QuizActivity quizActivity, View mDialogView, MucQuizActivityHelper mucQuizActivityHelper, int selectedClass, int selectedCategory ){
        this.quizActivity=quizActivity;
        this.mDialogView=mDialogView;
        this.mucQuizActivityHelper = mucQuizActivityHelper;
        this.sharedPrefHelper=new SharedPrefHelper(quizActivity);
        this.localDBHelper=new LocalDBHelper(quizActivity);
        this.selectedClass=selectedClass;
        this.selectedCategory=selectedCategory;
    }


    public void initEssayViews() {
        tv = mDialogView.findViewById(R.id.text_view_question);
        textViewCategory = mDialogView.findViewById(R.id.text_view_category);
        questionFeedback = mDialogView.findViewById(R.id.question_feedback);
        hiddenText = mDialogView.findViewById(R.id.hidden_text);
        myEditText = mDialogView.findViewById(R.id.edit_text);
    }

    public void showEssayQuestion() {
        saveEssayQuestionToPref();

        currentEssayQuestion=sharedPrefHelper.getCurrentEssayQuestion();

        mucQuizActivityHelper.hideRadioGroup();

        int categoryID = currentEssayQuestion.getCategoryID();

        categoryID--;

        currentCategory = categoryList.get(categoryID);
        categoryName = currentCategory.getName();

        myQuestion = currentEssayQuestion.getQuestion();

        myQuestion = myQuestion.replace("`", "'");

        setEssayQuestion(myQuestion, categoryName);

    }


    private void setEssayQuestion(String myQuestion, String categoryName) {

        tv.setText(myQuestion);

        textViewCategoryParams = (RelativeLayout.LayoutParams) textViewCategory.getLayoutParams();
        textViewCategoryParams.addRule(RelativeLayout.BELOW, R.id.text_view_question);
        textViewCategoryParams.setMargins(0, 50, 0, 0);

        textViewCategory.setText("Category: " + categoryName);

        buttonConfirmNext = mDialogView.findViewById(R.id.button_confirm_next);
        buttonEndQuiz = mDialogView.findViewById(R.id.button_end_quiz);
        buttonGotIt = mDialogView.findViewById(R.id.button_got_it);
        buttonFailedIt = mDialogView.findViewById(R.id.button_failed_it);


        buttonConfirmNextParams = (RelativeLayout.LayoutParams) buttonConfirmNext.getLayoutParams();

        buttonConfirmNextParams.addRule(RelativeLayout.BELOW, R.id.text_view_category);
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEssayAnswer();

            }
        });

    }

    private void showEssayAnswer() {

        setEssayAnswerViews();

        setEssayAnswer(currentEssayQuestion.getAnswer());

        quizActivity.cancelNotification();
        sharedPrefHelper.removeQuizShownBoolean();

    }

    private void setEssayAnswerViews() {
        questionFeedback.setVisibility(View.VISIBLE);
        buttonGotIt.setVisibility(View.VISIBLE);
        buttonFailedIt.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams questionFeedbackParams = (RelativeLayout.LayoutParams) questionFeedback.getLayoutParams();
        RelativeLayout.LayoutParams buttonGotItParams = (RelativeLayout.LayoutParams) buttonGotIt.getLayoutParams();
        RelativeLayout.LayoutParams buttonFailedItParams = (RelativeLayout.LayoutParams) buttonFailedIt.getLayoutParams();

        questionFeedbackParams.addRule(RelativeLayout.BELOW, R.id.text_view_category);

        buttonConfirmNext.setVisibility(View.GONE);
        buttonGotItParams.addRule(RelativeLayout.BELOW, R.id.question_feedback);
        buttonFailedItParams.addRule(RelativeLayout.BELOW, R.id.question_feedback);
        buttonFailedIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionFeedback.setVisibility(View.GONE);
                buttonGotIt.setVisibility(View.GONE);
                buttonFailedIt.setVisibility(View.GONE);

                reAddEssayQuestion();
                showEssayQuestion();

                myEditText.setText("");
                buttonConfirmNext.setVisibility(View.VISIBLE);
                buttonConfirmNext.setText("Submit");
            }
        });

        buttonGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.removeQuizShownBoolean();
                sharedPrefHelper.setEssayQuestionShownBoolean();
                quizActivity.finishAndRemoveTask();

            }
        });

    }

    private void setEssayAnswer(String correctAnswer) {
        questionFeedback.setText("The correct answer is " + correctAnswer + ".");
    }

    private void saveEssayQuestionToPref() {
        EssayQuestion currentEssayQuestion=null;
        int ctl = 0;

        categoryList = localDBHelper.getAllCategories(selectedClass);

        if (selectedCategory == 0) {
            essayQuestionList = localDBHelper.getAllEssayQuestions(selectedClass);
        } else {
            essayQuestionList = localDBHelper.getEssayQuestions(selectedClass, selectedCategory);
        }


        try {

            if (sharedPrefHelper.isSetExist()) {
                questionIndexSet = sharedPrefHelper.getQuestionIndexSet();

                questionIdList = new ArrayList<String>(questionIndexSet);

                ctl = questionIdList.size();

                Collections.shuffle(questionIdList);

                x = questionIdList.get(0);

                currentEssayQuestion = essayQuestionList.get(Integer.parseInt(x));

                if (ctl == 1) {
                    sharedPrefHelper.removeQuestionIndexSet();
                    quizActivity.showToast("Question index set removed.");
                } else {
                    questionIdList.remove(x);

                    ctl = questionIdList.size();
                    sharedPrefHelper.saveQuestionIndexSet(questionIdList);
                    if (failedQuestion) {
                        quizActivity.showToast("Question with ID " + previousQuestionID + " readded. " + ctl++ + " questions remaining.");
                    } else {
                        quizActivity.showToast(ctl + " questions remaining!");
                    }

                }


            } else {

                ctl = essayQuestionList.size();
                List<String> positions = new ArrayList<>();

                ctl--;

                int[] numbers = Util.getRangeArray(0, ctl);

                for (Integer nb : numbers) {
                    positions.add(String.valueOf(nb));
                }


                sharedPrefHelper.saveQuestionIndexSet(positions);

                Collections.shuffle(essayQuestionList);
                currentEssayQuestion = essayQuestionList.get(questionCounter);
                String y = String.valueOf(ctl);

                quizActivity.showToast(y + " questions remaining!");

            }

        } catch (Throwable e) {
            quizActivity.showToast(e.getMessage());
        }

        sharedPrefHelper.saveCurrentEssayQuestion(currentEssayQuestion);
    }

    private void reAddEssayQuestion() {

        if (sharedPrefHelper.isSetExist()) {
            questionIndexSet = sharedPrefHelper.getQuestionIndexSet();
            questionIdList = new ArrayList<String>(questionIndexSet);

            questionIdList.add(x);

            sharedPrefHelper.saveQuestionIndexSet(questionIdList);

            previousQuestionID = currentEssayQuestion.getId();
            failedQuestion = true;
        }
    }





}
