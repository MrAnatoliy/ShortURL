package com.shorturl.shorturl.services;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.shorturl.shorturl.DTOs.URLCreateDTO;
import com.shorturl.shorturl.DTOs.URLUpdateDTO;
import com.shorturl.shorturl.config.AppProperties;
import com.shorturl.shorturl.entities.ShortURLEntity;
import com.shorturl.shorturl.repositories.ShortURLRepository;


@Service
public class ShortURLService {

    Logger logger = Logger.getLogger(ShortURLService.class.getName());

    @Autowired private ShortURLRepository shortURLRepository;
    @Autowired private AppProperties appProperties;

    @SuppressWarnings("null")
    public ShortURLEntity createShortURL(URLCreateDTO urlCreateDTO) {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String newShortCode;

        do {
            newShortCode = NanoIdUtils.randomNanoId(new Random(), alphabet.toCharArray(), 30);
        } while(shortURLRepository.findByShortCode(newShortCode).orElse(null) != null);
        
        ShortURLEntity saved = shortURLRepository.save(
            ShortURLEntity.builder()
                .shortCode(newShortCode)
                .longURL(urlCreateDTO.getLongURL())
                .userUUID(urlCreateDTO.getUserUUID())
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofSeconds(appProperties.getSecondsOfTtl())))
                .maxRequests(appProperties.getRedirectAmount())
                .currentRequests(0)
                .build()
        );

        return saved;
    }

    public Optional<ShortURLEntity> getUrlByShortCode(String shortCode){
        return shortURLRepository.findByShortCode(shortCode);
    }

    public ShortURLEntity updateShortURLEntity(ShortURLEntity shortURLEntity){
        if(shortURLEntity == null){
            return null;
        }
        return shortURLRepository.save(shortURLEntity);
    }

    public void deleteShortURLEntiry(ShortURLEntity shortURLEntity){
        if(shortURLEntity == null){
            return;
        }
        shortURLRepository.delete(shortURLEntity);
    }

    @Transactional(readOnly = true)
    public List<ShortURLEntity> findAllByUser(UUID userUUID) {
        return shortURLRepository.findAllByUserUUID(userUUID);
    }

    @Transactional
    public ShortURLEntity updateURL(String shortCode, URLUpdateDTO dto) {
        ShortURLEntity url = shortURLRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!url.getUserUUID().equals(dto.getUserUUID())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your URL");
        }

        // apply only what came in the DTO
        if (dto.getMaxRequests() != null) {
            url.setMaxRequests(dto.getMaxRequests());
        }
        if (dto.getTtlSeconds() != null) {
            url.setExpiresAt(Instant.now().plusSeconds(dto.getTtlSeconds()));
        }

        return url; // @Transactional flushes
    }

    @Transactional
    public void deleteURL(String shortCode, UUID callerUUID) {
        ShortURLEntity url = shortURLRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!url.getUserUUID().equals(callerUUID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your URL");
        }

        shortURLRepository.delete(url);
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void cleanUpURLs() {
        Instant now = Instant.now();
        List<String> expired = shortURLRepository.findExpiredCodes(now);
        if (!expired.isEmpty()) {
            int deleted = shortURLRepository.deleteExpired(now);
            logger.info("Deleted %d expired URLs: %s".formatted(deleted, expired));
            // Real notificatoins can be sended here
        }
    }
}
