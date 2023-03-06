package kz.web.spring.urlShortener.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Url {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "original_url")
    @NotEmpty(message = "Cannot be empty")
    private String originalUrl;

    @Column(name = "short_url")
    @NotEmpty(message = "Cannot be empty")
    @Size(min = 5, max = 10, message = "Size of short url must be in range 5 to 10")
    private String shortUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

}
