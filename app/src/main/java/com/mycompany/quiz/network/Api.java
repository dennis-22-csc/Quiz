package com.mycompany.quiz.network;

public class Api {
    private static final String QUERY_URL = "https://aab-ng.000webhostapp.com/quiz_app/query_table.php?apicall=";

    public static final String URL_GET_QUESTIONS = QUERY_URL + "get_questions";
	public static final String URL_GET_ESSAY_QUESTIONS = QUERY_URL + "get_essay_questions";
    public static final String URL_GET_CATEGORIES = QUERY_URL + "get_categories"; 


    public static final String URL_GET_JAVA_QUESTIONS = QUERY_URL + "get_java_questions";
	public static final String URL_GET_JAVA_ESSAY_QUESTIONS = QUERY_URL + "get_java_essay_questions";
    public static final String URL_GET_JAVA_CATEGORIES = QUERY_URL + "get_java_categories";

    public static final String URL_GET_C_QUESTIONS = QUERY_URL + "get_c_questions";
    public static final String URL_GET_C_ESSAY_QUESTIONS = QUERY_URL + "get_c_essay_questions";
    public static final String URL_GET_C_CATEGORIES = QUERY_URL + "get_c_categories";

    public static final String URL_GET_LINUX_QUESTIONS = QUERY_URL + "get_linux_questions";
    public static final String URL_GET_LINUX_ESSAY_QUESTIONS = QUERY_URL + "get_linux_essay_questions";
    public static final String URL_GET_LINUX_CATEGORIES = QUERY_URL + "get_linux_categories";

}
