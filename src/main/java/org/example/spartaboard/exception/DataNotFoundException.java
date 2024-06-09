package org.example.spartaboard.exception;

public class DataNotFoundException extends RuntimeException  {
    //데이터가 존재하지 않는 경우에 발생
    public DataNotFoundException (String message) {
        super(message);
    }
}
