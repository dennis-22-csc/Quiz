package com.mycompany.quiz.database.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mycompany.quiz.database.QuizContract.*;



//TODO: Make db a singleton
public class LocalDBConfigurator extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
	private static LocalDBConfigurator mInstance=null;
	
    private LocalDBConfigurator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    } 

	@Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;


        //Java 
        final String SQL_CREATE_JAVA_CATEGORIES_TABLE = "CREATE TABLE " +
            CategoriesTable.JAVA_TABLE_NAME + "( " +
            CategoriesTable._ID + " INTEGER PRIMARY KEY, " +
            CategoriesTable.COLUMN_NAME + " TEXT UNIQUE " +
            ")";

		/**Multiple Choice*/
        final String SQL_CREATE_JAVA_QUESTIONS_TABLE = "CREATE TABLE " +
            QuestionsTable.JAVA_TABLE_NAME + " ( " +
            QuestionsTable._ID + " INTEGER PRIMARY KEY, " +
            QuestionsTable.COLUMN_QUESTION + " TEXT UNIQUE, " +
            QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
            QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
            QuestionsTable.COLUMN_OPTION3 + " TEXT, " + 
            QuestionsTable.COLUMN_OPTION4 + " TEXT, " + 
            QuestionsTable.COLUMN_CORRECT_OPTION + " TEXT, " + 
            QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
            "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
            CategoriesTable.JAVA_TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +

            ")"; 
		/**Essay */
		final String SQL_CREATE_JAVA_ESSAY_QUESTIONS_TABLE="CREATE TABLE " + 
		    EssayQuestionsTable.JAVA_TABLE_NAME + " ( " + 
			EssayQuestionsTable._ID + " INTEGER PRIMARY KEY, " + 
			EssayQuestionsTable.COLUMN_QUESTION + " TEXT UNIQUE, " + 
			EssayQuestionsTable.COLUMN_ANSWER + " TEXT, " + 
			EssayQuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " + 
		    "FOREIGN KEY(" + EssayQuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
            CategoriesTable.JAVA_TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +

            ")";

		//C
		final String SQL_CREATE_C_CATEGORIES_TABLE = "CREATE TABLE " +
				CategoriesTable.C_TABLE_NAME + "( " +
				CategoriesTable._ID + " INTEGER PRIMARY KEY, " +
				CategoriesTable.COLUMN_NAME + " TEXT UNIQUE " +
				")";

		/**Multiple Choice*/
		final String SQL_CREATE_C_QUESTIONS_TABLE = "CREATE TABLE " +
				QuestionsTable.C_TABLE_NAME + " ( " +
				QuestionsTable._ID + " INTEGER PRIMARY KEY, " +
				QuestionsTable.COLUMN_QUESTION + " TEXT UNIQUE, " +
				QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
				QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
				QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
				QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
				QuestionsTable.COLUMN_CORRECT_OPTION + " TEXT, " +
				QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
				"FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
				CategoriesTable.C_TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +

				")";
		/**Essay */
		final String SQL_CREATE_C_ESSAY_QUESTIONS_TABLE="CREATE TABLE " +
				EssayQuestionsTable.C_TABLE_NAME + " ( " +
				EssayQuestionsTable._ID + " INTEGER PRIMARY KEY, " +
				EssayQuestionsTable.COLUMN_QUESTION + " TEXT UNIQUE, " +
				EssayQuestionsTable.COLUMN_ANSWER + " TEXT, " +
				EssayQuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
				"FOREIGN KEY(" + EssayQuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
				CategoriesTable.C_TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +

				")";



		//Linux
		final String SQL_CREATE_LINUX_CATEGORIES_TABLE = "CREATE TABLE " +
				CategoriesTable.LINUX_TABLE_NAME + "( " +
				CategoriesTable._ID + " INTEGER PRIMARY KEY, " +
				CategoriesTable.COLUMN_NAME + " TEXT UNIQUE " +
				")";

		/**Multiple Choice*/
		final String SQL_CREATE_LINUX_QUESTIONS_TABLE = "CREATE TABLE " +
				QuestionsTable.LINUX_TABLE_NAME + " ( " +
				QuestionsTable._ID + " INTEGER PRIMARY KEY, " +
				QuestionsTable.COLUMN_QUESTION + " TEXT UNIQUE, " +
				QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
				QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
				QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
				QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
				QuestionsTable.COLUMN_CORRECT_OPTION + " TEXT, " +
				QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
				"FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
				CategoriesTable.LINUX_TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +

				")";
		/**Essay */
		final String SQL_CREATE_LINUX_ESSAY_QUESTIONS_TABLE="CREATE TABLE " +
				EssayQuestionsTable.LINUX_TABLE_NAME + " ( " +
				EssayQuestionsTable._ID + " INTEGER PRIMARY KEY, " +
				EssayQuestionsTable.COLUMN_QUESTION + " TEXT UNIQUE, " +
				EssayQuestionsTable.COLUMN_ANSWER + " TEXT, " +
				EssayQuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
				"FOREIGN KEY(" + EssayQuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
				CategoriesTable.LINUX_TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +

				")";


		//General
		final String SQL_CREATE_GENERAL_CATEGORIES_TABLE = "CREATE TABLE " +
			CategoriesTable.GENERAL_TABLE_NAME + "( " +
			CategoriesTable._ID + " INTEGER PRIMARY KEY, " +
			CategoriesTable.COLUMN_NAME + " TEXT UNIQUE " +
			")";

		/**Multiple Choice*/
        final String SQL_CREATE_GENERAL_QUESTIONS_TABLE = "CREATE TABLE " +
			QuestionsTable.GENERAL_TABLE_NAME + " ( " +
			QuestionsTable._ID + " INTEGER PRIMARY KEY, " +
			QuestionsTable.COLUMN_QUESTION + " TEXT UNIQUE, " +
			QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
			QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
			QuestionsTable.COLUMN_OPTION3 + " TEXT, " + 
			QuestionsTable.COLUMN_OPTION4 + " TEXT, " + 
			QuestionsTable.COLUMN_CORRECT_OPTION + " TEXT, " + 
			QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
			"FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
			CategoriesTable.GENERAL_TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +

			")";

		/**Essay */
		final String SQL_CREATE_GENERAL_ESSAY_QUESTIONS_TABLE="CREATE TABLE " + 
		    EssayQuestionsTable.GENERAL_TABLE_NAME + " ( " + 
			EssayQuestionsTable._ID + " INTEGER PRIMARY KEY, " + 
			EssayQuestionsTable.COLUMN_QUESTION + " TEXT UNIQUE, " + 
			EssayQuestionsTable.COLUMN_ANSWER + " TEXT, " + 
			EssayQuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " + 
			"FOREIGN KEY(" + EssayQuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
            CategoriesTable.GENERAL_TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +

            ")"; 


        db.execSQL(SQL_CREATE_JAVA_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_JAVA_QUESTIONS_TABLE);
		db.execSQL(SQL_CREATE_JAVA_ESSAY_QUESTIONS_TABLE);

		db.execSQL(SQL_CREATE_C_CATEGORIES_TABLE);
		db.execSQL(SQL_CREATE_C_QUESTIONS_TABLE);
		db.execSQL(SQL_CREATE_C_ESSAY_QUESTIONS_TABLE);

		db.execSQL(SQL_CREATE_LINUX_CATEGORIES_TABLE);
		db.execSQL(SQL_CREATE_LINUX_QUESTIONS_TABLE);
		db.execSQL(SQL_CREATE_LINUX_ESSAY_QUESTIONS_TABLE);

		db.execSQL(SQL_CREATE_GENERAL_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_GENERAL_QUESTIONS_TABLE);
		db.execSQL(SQL_CREATE_GENERAL_ESSAY_QUESTIONS_TABLE);

	} 

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 

        //General
		db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.GENERAL_TABLE_NAME);
	    db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.GENERAL_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EssayQuestionsTable.GENERAL_TABLE_NAME);

		//Java
		db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.JAVA_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.JAVA_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EssayQuestionsTable.JAVA_TABLE_NAME);

		//C
		db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.C_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.C_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EssayQuestionsTable.C_TABLE_NAME);

		//Linux
		db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.LINUX_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.LINUX_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + EssayQuestionsTable.LINUX_TABLE_NAME);


		onCreate(db);
    }   

	@Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    } 

	public static LocalDBConfigurator getInstance(Context context) {
		if (mInstance == null) {
            mInstance = new LocalDBConfigurator(context);
        }
        return mInstance;
    }

}
