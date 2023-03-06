package kz.web.spring.urlShortener.utils.errorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrlErrorResponse {

    private String message;
    private LocalDateTime time;

}
