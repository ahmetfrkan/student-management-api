package com.tpe.student_management.messages;

public class ErrorMessages {
    public static final String WRONG_DATE_FORMAT = "Invalid date format";
    public static final String CONTACT_MESSAGE_ID_NOT_FOUND = "No 'contact us' messages found by given ID: %s";
    public static final String UNIQUE_PROPERTY_VIOLATION = "%s already in use: %s";
    public static final String TEACHER_ALREADY_ADVISOR = "This teacher is already an advisor teacher.";
    public static final String TEACHER_ALREADY_NOT_ADVISOR = "This teacher is already not an advisor.";
    public static final String ENTITY_NOT_FOUND = "No %s found by given %s: %s";
    public static final String TERM_CONFLICTS = "Term dates are conflicting.";
    public static final String EDUCATION_TERM_ALREADY_EXISTS = "This term already exists in the same year.";
    public static final String TERM_END_EARLIER_THAN_START = "Term end date is earlier than the start date.";
    public static final String TERM_START_EARLIER_THAN_LAST_REGISTRATION = "Term start date is earlier than the last registration date.";
}
