package com.shorturl.shorturl.controllers;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shorturl.shorturl.DTOs.URLCreateDTO;
import com.shorturl.shorturl.DTOs.URLResponseDTO;
import com.shorturl.shorturl.DTOs.URLUpdateDTO;
import com.shorturl.shorturl.config.AppProperties;
import com.shorturl.shorturl.entities.ShortURLEntity;
import com.shorturl.shorturl.services.ShortURLService;

import jakarta.validation.Valid;

@RestController
public class URLAdminController {

    @Autowired ShortURLService shortURLService;
    @Autowired AppProperties appProperties;

    Logger logger = Logger.getLogger(URLAdminController.class.getName());

    @PostMapping("/createShortUrl")
    ResponseEntity<URLResponseDTO> createShortURL(@Valid @RequestBody URLCreateDTO urlCreateDTO) {
        UUID userUUID;

        logger.info(appProperties.toString());

        if(urlCreateDTO.getUserUUID() == null){
            logger.info("User has no UUID yet, creating new UUID");
            userUUID = UUID.randomUUID();
        } else {
            userUUID = urlCreateDTO.getUserUUID();
        }

        urlCreateDTO.setUserUUID(userUUID);

        logger.info("Creating new shortURL for :\nlongURL : " + urlCreateDTO.getLongURL() + "\nUUID : " + userUUID);
        ShortURLEntity created = shortURLService.createShortURL(urlCreateDTO);

        if(created != null){
            logger.info("New Short URL was created successfully! : " + created.toString());
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(URLResponseDTO.tDto(created, appProperties));
        } else {
            logger.info("Failed to create new short url : created url in null");
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    @GetMapping("/urls/user/{userUUID}")
    public ResponseEntity<List<URLResponseDTO>> getUserURLs(
            @PathVariable UUID userUUID) {

        List<URLResponseDTO> list = shortURLService.findAllByUser(userUUID)
                .stream()
                .map(e -> URLResponseDTO.tDto(e, appProperties))
                .toList();

        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    @PatchMapping("/urls/{shortCode}")
    public ResponseEntity<URLResponseDTO> updateURL(
            @PathVariable String shortCode,
            @Valid @RequestBody URLUpdateDTO dto
    ) {
        ShortURLEntity updated = shortURLService.updateURL(shortCode, dto);
        return ResponseEntity.ok(URLResponseDTO.tDto(updated, appProperties));
    }

    @DeleteMapping("/urls/{shortCode}")
    public ResponseEntity<Void> deleteURL(
            @PathVariable String shortCode,
            @Valid @RequestBody URLUpdateDTO dto
    ) {
        shortURLService.deleteURL(shortCode, dto.getUserUUID());
        return ResponseEntity.noContent().build();
    }
}