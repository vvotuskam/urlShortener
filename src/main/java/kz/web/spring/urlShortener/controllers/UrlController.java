package kz.web.spring.urlShortener.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kz.web.spring.urlShortener.dto.UrlRequestDTO;
import kz.web.spring.urlShortener.dto.UrlResponseDTO;
import kz.web.spring.urlShortener.models.Url;
import kz.web.spring.urlShortener.services.UrlService;
import kz.web.spring.urlShortener.utils.errorResponse.UrlErrorResponse;
import kz.web.spring.urlShortener.utils.exceptions.UrlException;
import kz.web.spring.urlShortener.utils.exceptions.UrlExpiredException;
import kz.web.spring.urlShortener.utils.exceptions.UrlNotFoundException;
import kz.web.spring.urlShortener.utils.exceptions.UrlNotValidException;
import kz.web.spring.urlShortener.utils.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UrlController {

    private final UrlService urlService;
    private final UrlValidator urlValidator;

    @Autowired
    public UrlController(UrlService urlService, UrlValidator urlValidator) {
        this.urlService = urlService;
        this.urlValidator = urlValidator;
    }

    @GetMapping()
    public List<UrlResponseDTO> index() {
        return urlService.getAll().stream().map(urlService::convertToResponseDTO).collect(Collectors.toList());
    }

    @PostMapping("/register")
    public UrlResponseDTO register(@RequestBody @Valid UrlRequestDTO request,
                                   BindingResult result) {

        urlValidator.validate(urlService.convertToUrl(request), result);

        if (result.hasErrors()) {
            StringBuilder message = new StringBuilder();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                message.append(error.getDefaultMessage()).append(". ");
            }
            throw new UrlNotValidException(message.toString().trim());
        }

        UrlResponseDTO response = urlService.register(request);
        return response;
     }

    @GetMapping("/{shortUrl}")
    public void get(@PathVariable("shortUrl") String shortUrl,
                    HttpServletResponse response) {

        if (shortUrl.isBlank()) {
            throw new UrlNotValidException("Url cannot be blank");
        }

        Optional<Url> urlOptional = urlService.getByShortUrl(shortUrl);
        if (urlOptional.isEmpty()) {
            throw new UrlNotFoundException("Short URL \"" + shortUrl + "\" is not found");
        }

        Url url = urlOptional.get();
        if (url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlExpiredException("URL is expired");
        }

        try {
            response.sendRedirect(url.getOriginalUrl());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler
    public ResponseEntity<UrlErrorResponse> handleException(UrlException e) {
        UrlErrorResponse response = new UrlErrorResponse(
                e.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }
}
