package ru.otus.lib.exception;

public class AuthorContainsLinksException extends RuntimeException{

    private static final long serialVersionUID = 6143973447936840267L;

    public AuthorContainsLinksException(String message) {
        super(message);
    }
    
    

}
