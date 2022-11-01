package com.mycompany.quiz.quiz;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mycompany.quiz.R;
import com.mycompany.quiz.database.local.LocalDBHelper;
import com.mycompany.quiz.database.local.SharedPrefHelper;
import com.mycompany.quiz.domain.Category;
import com.mycompany.quiz.domain.Question;
import com.mycompany.quiz.utilities.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MucQuizActivityHelper {
    List<Question> questionList;
    List<Category> categoryList;

    Set<String> questionIndexSet;
    List<String> questionIdList;
    String x;
    boolean failedQuestion;
    Question currentQuestion;
    Category currentCategory;
    int previousQuestionID;
    int questionCounter;
    String myQuestion;
    String categoryName;


    RadioGroup rbGroup;
    RadioButton rd1, rd2, rd3, rd4, rbSelected;
    Button buttonEndQuiz, buttonConfirmNext;
    TextView tv, textViewCategory, questionFeedback, hiddenText;

    RelativeLayout.LayoutParams buttonConfirmNextParams, textViewCategoryParams;


    String rbText;

    View mDialogView;
    QuizActivity quizActivity;

    SharedPrefHelper sharedPrefHelper;
    LocalDBHelper localDBHelper;
    Util util;

    int selectedClass;
    int selectedCategory;

    public MucQuizActivityHelper(QuizActivity quizActivity, View mDialogView, int selectedClass, int selectedCategory){
        this.quizActivity=quizActivity;
        this.mDialogView=mDialogView;
        this.sharedPrefHelper=new SharedPrefHelper(quizActivity);
        this.localDBHelper=new LocalDBHelper(quizActivity);
        this.selectedClass=selectedClass;
        this.selectedCategory=selectedCategory;
    }

    public void initMucViews() {
        tv = mDialogView.findViewById(R.id.text_view_question);
        rd1 = mDialogView.findViewById(R.id.radio_button1);
        rd2 = mDialogView.findViewById(R.id.radio_button2);
        rd3 = mDialogView.findViewById(R.id.radio_button3);
        rd4 = mDialogView.findViewById(R.id.radio_button4);
        textViewCategory = mDialogView.findViewById(R.id.text_view_category);
        questionFeedback = mDialogView.findViewById(R.id.question_feedback);
        rbGroup = mDialogView.findViewById(R.id.radio_group);
        hiddenText = mDialogView.findViewById(R.id.hidden_text);
    }


    public void showMCQuestion() {
        saveQuestionToPref();

        currentQuestion=sharedPrefHelper.getCurrentMucQuestion();

        int categoryID = currentQuestion.getCategoryID();

        categoryID--;

        currentCategory = categoryList.get(categoryID);
        categoryName = currentCategory.getName();

        myQuestion = currentQuestion.getQuestion();
        String fOption = currentQuestion.getOption1();
        String sOption = currentQuestion.getOption2();
        String tOption = currentQuestion.getOption3();
        String fthOption = currentQuestion.getOption4();

        myQuestion = myQuestion.replace("`", "'");
        fOption = fOption.replace("`", "'");
        sOption = sOption.replace("`", "'");
        tOption = tOption.replace("`", "'");
        fthOption = fthOption.replace("`", "'");

        List<String> a1 = new ArrayList<>();
        a1.add(fOption);
        a1.add(sOption);
        a1.add(tOption);
        a1.add(fthOption);

        Collections.shuffle(a1);

        String s1 = a1.get(0);
        String s2 = a1.get(1);
        String s3 = a1.get(2);
        String s4 = a1.get(3);


        setMCQuestion(myQuestion, s1, s2, s3, s4, categoryName);
    }

    public void hideRadioGroup() {
        rbGroup.setVisibility(View.GONE);
    }



    private void setMCQuestion(String myQuestion, String s1, String s2, String s3, String s4, String categoryName) {

        tv.setText(myQuestion);

        rd1.setText(s1);
        rd2.setText(s2);
        rd3.setText(s3);
        rd4.setText(s4);

        textViewCategoryParams = (RelativeLayout.LayoutParams) textViewCategory.getLayoutParams();
        textViewCategoryParams.addRule(RelativeLayout.BELOW, R.id.radio_group);

        textViewCategory.setText("Category: " + categoryName);

        buttonConfirmNext = mDialogView.findViewById(R.id.button_confirm_next);
        buttonEndQuiz = mDialogView.findViewById(R.id.button_end_quiz);

        buttonConfirmNextParams = (RelativeLayout.LayoutParams) buttonConfirmNext.getLayoutParams();

        buttonConfirmNextParams.addRule(RelativeLayout.BELOW, R.id.text_view_category);
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rd1.isChecked() || rd2.isChecked() || rd3.isChecked() || rd4.isChecked()) {
                    showMCAnswer();
                } else {
                    quizActivity.showToast("Please select an answer");
                }
            }
        });

    }

    private void setMCViews(Question currentQuestion, String correctOption) {

        questionFeedback.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams questionFeedbackParams = (RelativeLayout.LayoutParams) questionFeedback.getLayoutParams();
        RelativeLayout.LayoutParams buttonConfirmNextParams = (RelativeLayout.LayoutParams) buttonConfirmNext.getLayoutParams();
        RelativeLayout.LayoutParams buttonEndQuizParams = (RelativeLayout.LayoutParams) buttonEndQuiz.getLayoutParams();
        RelativeLayout.LayoutParams hiddenTextParams = (RelativeLayout.LayoutParams) hiddenText.getLayoutParams();

        questionFeedbackParams.addRule(RelativeLayout.BELOW, R.id.text_view_category);

        rbSelected = mDialogView.findViewById(rbGroup.getCheckedRadioButtonId());
        rbText = rbSelected.getText().toString();

        if (rbText.equals(currentQuestion.getCorrectOption())) {

            buttonConfirmNext.setVisibility(View.GONE);
            buttonEndQuiz.setVisibility(View.VISIBLE);
            buttonEndQuizParams.addRule(RelativeLayout.BELOW, R.id.question_feedback);
            questionFeedback.setText("Your answer is correct!");
            hiddenText.setVisibility(View.VISIBLE);
            hiddenTextParams.addRule(RelativeLayout.BELOW, R.id.button_end_quiz);
            hiddenText.requestFocus();
            sharedPrefHelper.removeQuizShownBoolean();
            sharedPrefHelper.setMucQuestionShownBoolean();

        } else {

            buttonConfirmNextParams.addRule(RelativeLayout.BELOW, R.id.question_feedback);
            hiddenText.setVisibility(View.VISIBLE);
            hiddenTextParams.addRule(RelativeLayout.BELOW, R.id.button_confirm_next);
            hiddenText.requestFocus();
            questionFeedback.setText("Your answer is not correct!");
            buttonConfirmNext.setText("Okay");
            buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rbGroup.clearCheck();
                    questionFeedback.setVisibility(View.GONE);

                    reAddMCQuestion();
                    showMCQuestion();

                    buttonConfirmNext.setText("Submit");
                }
            });
        }
    }

    private void saveQuestionToPref() {
        Question currentQuestion=null;

        int ctl = 0;

        categoryList = localDBHelper.getAllCategories(selectedClass);

        if (selectedCategory == 0) {
            questionList = localDBHelper.getAllQuestions(selectedClass);
        } else {
            questionList = localDBHelper.getQuestions(selectedClass, selectedCategory);
        }

        try {

            if (sharedPrefHelper.isSetExist()) {
                questionIndexSet = sharedPrefHelper.getQuestionIndexSet();

                questionIdList = new ArrayList<String>(questionIndexSet);

                ctl = questionIdList.size();

                Collections.shuffle(questionIdList);

                x = questionIdList.get(0);

                currentQuestion = questionList.get(Integer.parseInt(x));

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

                ctl = questionList.size();
                List<String> positions = new ArrayList<>();

                ctl--;

                int[] numbers = Util.getRangeArray(0, ctl);

                for (Integer nb : numbers) {
                    positions.add(String.valueOf(nb));
                }


                sharedPrefHelper.saveQuestionIndexSet(positions);

                Collections.shuffle(questionList);
                currentQuestion = questionList.get(questionCounter);
                String y = String.valueOf(ctl);

                quizActivity.showToast(y + " questions remaining!");

            }

        } catch (Throwable e) {
            quizActivity.showToast(e.getMessage());
        }

        sharedPrefHelper.saveCurrentMucQuestion(currentQuestion);

    }

    private void showMCAnswer() {

        quizActivity.cancelNotification();

        setMCViews(currentQuestion, currentQuestion.getCorrectOption());

    }


    private void reAddMCQuestion() {

        if (sharedPrefHelper.isSetExist()) {
            questionIndexSet = sharedPrefHelper.getQuestionIndexSet();
            questionIdList = new ArrayList<String>(questionIndexSet);

            questionIdList.add(x);

            sharedPrefHelper.saveQuestionIndexSet(questionIdList);

            previousQuestionID = currentQuestion.getId();
            failedQuestion = true;
        }
    }

}
