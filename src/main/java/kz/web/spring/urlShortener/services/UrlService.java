package kz.web.spring.urlShortener.services;

import kz.web.spring.urlShortener.dto.UrlRequestDTO;
import kz.web.spring.urlShortener.dto.UrlResponseDTO;
import kz.web.spring.urlShortener.models.Url;
import kz.web.spring.urlShortener.repositories.UrlRepository;
import kz.web.spring.urlShortener.utils.urlShortener.UrlShortener;
import kz.web.spring.urlShortener.utils.exceptions.UrlNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlShortener urlShortener;
    private final ModelMapper mapper;

    @Autowired
    public UrlService(UrlRepository urlRepository, UrlShortener urlShortener, ModelMapper mapper) {
        this.urlRepository = urlRepository;
        this.urlShortener = urlShortener;
        this.mapper = mapper;
    }

    public String generateShortUrl(String originalUrl) {
        return urlShortener.encode(originalUrl);
    }

    public List<Url> getAll() {
        return urlRepository.findAll();
    }

    public Optional<Url> getByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    public Optional<Url> getByOriginalUrl(String originalUrl) {
        return urlRepository.findByOriginalUrl(originalUrl);
    }
    public Optional<Url> get(int id) {
        return urlRepository.findById(id);
    }

    @Transactional
    public Url save(Url url) {
        return urlRepository.save(url);
    }

    @Transactional
    public Url update(int id, Url updatedUrl) {
        Optional<Url> urlOptional = get(id);
        if (urlOptional.isEmpty()) {
            throw new UrlNotFoundException("URL with id " + id + " is not found");
        }
        updatedUrl.setId(id);
        return urlRepository.save(updatedUrl);
    }

    @Transactional
    public void delete(int id) {
        urlRepository.deleteById(id);
    }

    @Transactional
    public UrlResponseDTO register(UrlRequestDTO request) {
        String originalUrl = request.getOriginalUrl();
        String shortUrl = generateShortUrl(originalUrl);

        Url url = convertToUrl(request);

        url.setShortUrl(shortUrl);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiresAt(LocalDateTime.now().plusYears(1));

        save(url);

        UrlResponseDTO response = convertToResponseDTO(url);
        return response;
    }

    public UrlResponseDTO convertToResponseDTO(Url url) {
        return mapper.map(url, UrlResponseDTO.class);
    }

    public UrlRequestDTO convertToRequestDTO(Url url) {
        return mapper.map(url, UrlRequestDTO.class);
    }

    public Url convertToUrl(UrlResponseDTO responseDTO) {
        return mapper.map(responseDTO, Url.class);
    }

    public Url convertToUrl(UrlRequestDTO requestDTO) {
        return mapper.map(requestDTO, Url.class);
    }
}
