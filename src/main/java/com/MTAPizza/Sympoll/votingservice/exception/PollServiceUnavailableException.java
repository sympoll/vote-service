package com.MTAPizza.Sympoll.votingservice.exception;

public class PollServiceUnavailableException extends  RuntimeException{
    // Default constructor
    public PollServiceUnavailableException() {
        super("The poll service is currently unavailable.");
    }

    // Constructor that accepts a custom message
    public PollServiceUnavailableException(String message) {
        super(message);
    }

    // Constructor that accepts a custom message and a cause
    public PollServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public PollServiceUnavailableException(Throwable cause) {
        super(cause);
    }
}
