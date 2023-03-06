package kz.web.spring.urlShortener.utils.exceptions;

public class UrlNotFoundException extends UrlException {
    public UrlNotFoundException(String message) {
        super(message);
    }
}
