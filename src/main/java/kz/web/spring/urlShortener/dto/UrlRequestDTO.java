package kz.web.spring.urlShortener.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlRequestDTO {

    @NotEmpty(message = "Cannot be empty")
    private String originalUrl;
}
