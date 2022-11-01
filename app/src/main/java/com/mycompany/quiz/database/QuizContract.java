package com.mycompany.quiz.database;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {
    }

	public static class CategoriesTable implements BaseColumns {
        public static final String JAVA_TABLE_NAME = "quiz_java_categories";
		public static final String LINUX_TABLE_NAME = "quiz_linux_categories";
		public static final String C_TABLE_NAME = "quiz_c_categories";

		public static final String GENERAL_TABLE_NAME = "quiz_general_categories";

		public static final String COLUMN_NAME = "name";
    } 

    public static class QuestionsTable implements BaseColumns {
        public static final String JAVA_TABLE_NAME = "quiz_java_questions";
		public static final String LINUX_TABLE_NAME = "quiz_linux_questions";
		public static final String GENERAL_TABLE_NAME = "quiz_general_questions";
		public static final String C_TABLE_NAME = "quiz_c_questions";


		public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
		public static final String COLUMN_OPTION4 = "option4";
		public static final String COLUMN_CORRECT_OPTION = "correct_option";
		public static final String COLUMN_CATEGORY_ID = "category_id";
	}

	public static class EssayQuestionsTable implements BaseColumns {
        public static final String JAVA_TABLE_NAME = "quiz_java_essay_questions";
		public static final String LINUX_TABLE_NAME = "quiz_linux_essay_questions";
		public static final String GENERAL_TABLE_NAME = "quiz_general_essay_questions";
		public static final String C_TABLE_NAME = "quiz_c_essay_questions";


		public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER = "answer";
		public static final String COLUMN_CATEGORY_ID = "category_id";
	}

}
