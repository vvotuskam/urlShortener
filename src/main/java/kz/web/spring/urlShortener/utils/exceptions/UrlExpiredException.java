package kz.web.spring.urlShortener.utils.exceptions;

public class UrlExpiredException extends UrlException {
    public UrlExpiredException(String message) {
        super(message);
    }
}
