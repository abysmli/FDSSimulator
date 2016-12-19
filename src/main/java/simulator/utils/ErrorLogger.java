/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.utils;

/**
 *
 * @author abysmli
 */
public class ErrorLogger {

    /**
     * Used for error messages.
     */
    public static final int ERROR_MESSAGE = 0;
    /**
     * Used for information messages.
     */
    public static final int INFORMATION_MESSAGE = 1;
    /**
     * Used for warning messages.
     */
    public static final int WARNING_MESSAGE = 2;
    /**
     * Used for questions.
     */
    public static final int QUESTION_MESSAGE = 3;
    /**
     * No icon is used.
     */
    public static final int PLAIN_MESSAGE = -1;

    public static void log(Exception e, String Title, String Message, int LogType) {
        e.printStackTrace(System.err);
    }

}
