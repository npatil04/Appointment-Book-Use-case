package com.example.appointmentsystem.exception;

public class SlotOverlapException extends RuntimeException {

    public SlotOverlapException(String message) {
        super(message);
    }
}
