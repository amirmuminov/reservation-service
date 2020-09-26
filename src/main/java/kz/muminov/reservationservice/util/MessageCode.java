package kz.muminov.reservationservice.util;

public enum MessageCode {

    TABLE_DOES_NOT_EXIST(1, "Table does not exist"),
    THIS_RESERVATION_TIME_IS_NOT_FREE(2, "This reservation time is not free"),
    START_DATE_IS_BIGGER_THAN_END_DATE(3, "Start reservation date is bigger than end date");

    int errorCode;
    private String defaultMessage;

    MessageCode(int code, String defaultMessage){
        this.errorCode = code;
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
