package kz.web.spring.urlShortener.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponseDTO {

    @NotEmpty(message = "Cannot be empty")
    private String originalUrl;

    @NotEmpty(message = "Cannot be empty")
    @Size(min = 5, max = 10, message = "Size of short url must be in range 5 to 10")
    private String shortUrl;
}
