package com.mycompany.quiz.database.local;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import com.mycompany.quiz.domain.Category;
import com.mycompany.quiz.domain.EssayQuestion;
import com.mycompany.quiz.domain.Question;
import com.mycompany.quiz.database.QuizContract.*;


import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;


public class LocalDBHelper {

	private Cursor c;
	private SQLiteDatabase rDatabase; 
	private SQLiteDatabase wDatabase;
	private long count;

	Context context;

	public LocalDBHelper(Context context) { 
        LocalDBConfigurator dbConfigurator=LocalDBConfigurator.getInstance(context);
		rDatabase=dbConfigurator.getReadableDatabase();
		wDatabase=dbConfigurator.getWritableDatabase();

		this.context=context;
	}
	
	public void addCategories(List<Category> categories, int selectedClass) {

        for (Category category : categories) {

            ContentValues cv = new ContentValues();
            cv.put(CategoriesTable.COLUMN_NAME, category.getName());

			//TODO: Replace int with enums
            if (selectedClass==1) { 
                wDatabase.insert(CategoriesTable.JAVA_TABLE_NAME, null, cv);

            } else if (selectedClass==2) {
                wDatabase.insert(CategoriesTable.C_TABLE_NAME, null, cv);

            } else if (selectedClass==3) {
                wDatabase.insert(CategoriesTable.LINUX_TABLE_NAME, null, cv);

            } else if (selectedClass==4) {
                wDatabase.insert(CategoriesTable.GENERAL_TABLE_NAME, null, cv);

            }

        }
    }

    @SuppressLint("Range")
    public List<Category> getAllCategories(int selectedClass) {
        List<Category> categoryList = new ArrayList<>();

        if (selectedClass==1) {
            c = rDatabase.rawQuery("SELECT * FROM " + CategoriesTable.JAVA_TABLE_NAME, null);

        } else if (selectedClass==2) {
            c = rDatabase.rawQuery("SELECT * FROM " + CategoriesTable.C_TABLE_NAME, null);

        } else if (selectedClass==3) {
            c = rDatabase.rawQuery("SELECT * FROM " + CategoriesTable.LINUX_TABLE_NAME, null);

        } else if (selectedClass==4) {
            c = rDatabase.rawQuery("SELECT * FROM " + CategoriesTable.GENERAL_TABLE_NAME, null);

        }


        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

		// c.close();
        return categoryList;
    } 
	
	public void addMCQuestions(List<Question> questions, int selectedClass) {

        for (Question question : questions) { 
            ContentValues cv = new ContentValues();
            cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
            cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
            cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
            cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3()); 
            cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
            cv.put(QuestionsTable.COLUMN_CORRECT_OPTION, question.getCorrectOption());
            cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());

			//TODO: Replace int with enum
            if (selectedClass==1) { 
                wDatabase.insert(QuestionsTable.JAVA_TABLE_NAME, null, cv);

            } else if (selectedClass==2) {
                wDatabase.insert(QuestionsTable.C_TABLE_NAME, null, cv);

            } else if (selectedClass==3) {
                wDatabase.insert(QuestionsTable.LINUX_TABLE_NAME, null, cv);

            } else if (selectedClass==4) {
                wDatabase.insert(QuestionsTable.GENERAL_TABLE_NAME, null, cv);

            }

        }
    } 

	public void addEssayQuestions(List<EssayQuestion> questions, int selectedClass) {

        for (EssayQuestion question : questions) { 
            ContentValues cv = new ContentValues();
            cv.put(EssayQuestionsTable.COLUMN_QUESTION, question.getQuestion());
            cv.put(EssayQuestionsTable.COLUMN_ANSWER, question.getAnswer());
            cv.put(EssayQuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());

			//TODO: Replace int with enum
            if (selectedClass==1) { 
                wDatabase.insert(EssayQuestionsTable.JAVA_TABLE_NAME, null, cv);

            } else if (selectedClass==2) {
                wDatabase.insert(EssayQuestionsTable.C_TABLE_NAME, null, cv);

            } else if (selectedClass==3) {
                wDatabase.insert(EssayQuestionsTable.LINUX_TABLE_NAME, null, cv);

            } else if (selectedClass==4) {
                wDatabase.insert(EssayQuestionsTable.GENERAL_TABLE_NAME, null, cv);

            }

        }
    }

    @SuppressLint("Range")
    public ArrayList<EssayQuestion> getEssayQuestions(int selectedClass, int categoryID) {


        ArrayList<EssayQuestion> questionList = new ArrayList<>();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID)};
        if (selectedClass==1) {
            c = rDatabase.query(
                    EssayQuestionsTable.JAVA_TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        } else if (selectedClass==2) {
            c = rDatabase.query(
                    EssayQuestionsTable.C_TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        } else if (selectedClass==3) {
            c = rDatabase.query(
                    EssayQuestionsTable.LINUX_TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        } else if (selectedClass==4) {
            c = rDatabase.query(
                    EssayQuestionsTable.GENERAL_TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        }



        if (c.moveToFirst()) {
            do {
                EssayQuestion question = new EssayQuestion();
                question.setId(c.getInt(c.getColumnIndex(EssayQuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(EssayQuestionsTable.COLUMN_QUESTION)));
                question.setAnswer(c.getString(c.getColumnIndex(EssayQuestionsTable.COLUMN_ANSWER)));
                question.setCategoryID(c.getInt(c.getColumnIndex(EssayQuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;

    }


    @SuppressLint("Range")
    public ArrayList<Question> getQuestions(int selectedClass, int categoryID) {


        ArrayList<Question> questionList = new ArrayList<>();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID)};
        if (selectedClass==1) {
            c = rDatabase.query(
                    QuestionsTable.JAVA_TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        } else if (selectedClass==2) {
            c = rDatabase.query(
                    QuestionsTable.C_TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        } else if (selectedClass==3) {
            c = rDatabase.query(
                    QuestionsTable.LINUX_TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        } else if (selectedClass==4) {
            c = rDatabase.query(
                    QuestionsTable.GENERAL_TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        }


        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setCorrectOption(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_CORRECT_OPTION)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;


    }





    @SuppressLint("Range")
    public List<Question> getAllQuestions(int selectedClass) {
        List<Question> questionList = new ArrayList<>();

        if (selectedClass==1) {
            c = rDatabase.rawQuery("SELECT * FROM " + QuestionsTable.JAVA_TABLE_NAME, null);

        } else if (selectedClass==2) {
            c = rDatabase.rawQuery("SELECT * FROM " + QuestionsTable.C_TABLE_NAME, null);

        } else if (selectedClass==3) {
            c = rDatabase.rawQuery("SELECT * FROM " + QuestionsTable.LINUX_TABLE_NAME, null);

        } else if (selectedClass==4) {
            c = rDatabase.rawQuery("SELECT * FROM " + QuestionsTable.GENERAL_TABLE_NAME, null);

        }

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setCorrectOption(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_CORRECT_OPTION)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    @SuppressLint("Range")
    public List<EssayQuestion> getAllEssayQuestions(int selectedClass) {
        List<EssayQuestion> questionList = new ArrayList<>();

        if (selectedClass==1) {
            c = rDatabase.rawQuery("SELECT * FROM " + EssayQuestionsTable.JAVA_TABLE_NAME, null);

        } else if (selectedClass==2) {
            c = rDatabase.rawQuery("SELECT * FROM " + EssayQuestionsTable.C_TABLE_NAME, null);

        } else if (selectedClass==3) {
            c = rDatabase.rawQuery("SELECT * FROM " + EssayQuestionsTable.LINUX_TABLE_NAME, null);

        } else if (selectedClass==4) {
            c = rDatabase.rawQuery("SELECT * FROM " + EssayQuestionsTable.GENERAL_TABLE_NAME, null);

        }

        if (c.moveToFirst()) {
            do {
                EssayQuestion question = new EssayQuestion();
                question.setId(c.getInt(c.getColumnIndex(EssayQuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(EssayQuestionsTable.COLUMN_QUESTION)));
                question.setAnswer(c.getString(c.getColumnIndex(EssayQuestionsTable.COLUMN_ANSWER)));
                question.setCategoryID(c.getInt(c.getColumnIndex(EssayQuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }



    /*public long countQuestions(int selectedClass, int selectedCategory, int selectedQuestionType) {

          if (selectedQuestionType==1) {
            return countMultipleChoiceQuestions(selectedClass,selectedCategory);
          } else if(selectedQuestionType==2) {
            return countEssayQuestions(selectedClass, selectedCategory);
          }
          return 0;
	}

	private long countMultipleChoiceQuestions(int selectedClass, int selectedCategory) {
        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? ";

        String[] selectionArgs = new String[]{String.valueOf(selectedCategory)};


        if (selectedClass==1&&selectedCategory==1) {
            count= DatabaseUtils.queryNumEntries(rDatabase, QuestionsTable.JAVA_TABLE_NAME);
        } else if (selectedClass==1&&selectedCategory!=1) {
            count=DatabaseUtils.queryNumEntries(rDatabase, QuestionsTable.JAVA_TABLE_NAME, selection, selectionArgs);
        } else if (selectedClass==2) {
            count=DatabaseUtils.queryNumEntries(rDatabase, QuestionsTable.GENERAL_TABLE_NAME, selection, selectionArgs);
        }

        //rDatabase.close();

        return count;

    }

	private long countEssayQuestions(int selectedClass, int selectedCategory) {
		String selection = EssayQuestionsTable.COLUMN_CATEGORY_ID + " = ? "; 

        String[] selectionArgs = new String[]{String.valueOf(selectedCategory)};


        if (selectedClass==1&&selectedCategory==1) { 
			count= DatabaseUtils.queryNumEntries(rDatabase, EssayQuestionsTable.JAVA_TABLE_NAME);
        } else if (selectedClass==1&&selectedCategory!=1) {
			count=DatabaseUtils.queryNumEntries(rDatabase, EssayQuestionsTable.JAVA_TABLE_NAME, selection, selectionArgs);
        } else if (selectedClass==2) {
			count=DatabaseUtils.queryNumEntries(rDatabase, EssayQuestionsTable.GENERAL_TABLE_NAME, selection, selectionArgs);
		}



		return count;
	}*/


}
