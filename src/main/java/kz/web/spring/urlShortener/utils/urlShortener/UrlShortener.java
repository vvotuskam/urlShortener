package kz.web.spring.urlShortener.utils.urlShortener;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class UrlShortener {

    public String encode(String originalUrl) {
        LocalDateTime time = LocalDateTime.now();

        String encodedURL = Hashing.murmur3_32_fixed()
                .hashString(originalUrl.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();

        return encodedURL;
    }
}
