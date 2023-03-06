package kz.web.spring.urlShortener.utils.validator;

import kz.web.spring.urlShortener.models.Url;
import kz.web.spring.urlShortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UrlValidator implements Validator {

    private final UrlService service;

    @Autowired
    public UrlValidator(UrlService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Url.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Url url = (Url) target;
        Optional<Url> urlOptional = service.getByOriginalUrl(url.getOriginalUrl());
        if (urlOptional.isPresent()) {
            errors.rejectValue("originalUrl", "", "This URL has been already registered");
        }
    }
}
