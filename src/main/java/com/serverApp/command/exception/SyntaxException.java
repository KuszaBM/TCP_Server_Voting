package com.serverApp.command.exception;

public class SyntaxException extends Throwable {
    public final String reason;
    public static final String UNRECOGNIZED = "Unrecognized command found";

    public SyntaxException(String reason)
    {
        this.reason = reason;
    }
}
