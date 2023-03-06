package kz.web.spring.urlShortener.utils.exceptions;

public class UrlException extends RuntimeException {
    public UrlException(String message) {
        super(message);
    }
    public UrlException() {

    }
}
