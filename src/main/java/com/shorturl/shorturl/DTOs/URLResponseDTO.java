package com.shorturl.shorturl.DTOs;

import java.time.Instant;

import com.shorturl.shorturl.config.AppProperties;
import com.shorturl.shorturl.entities.ShortURLEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class URLResponseDTO extends URLBaseDTO {

    public static URLResponseDTO tDto(ShortURLEntity entity, AppProperties appProperties) {
        URLResponseDTO newURLResponseDTO = new URLResponseDTO();
        newURLResponseDTO.setUserUUID(entity.getUserUUID());
        newURLResponseDTO.setLongURL(entity.getLongURL());
        newURLResponseDTO.setShortURL(appProperties.getRedirectUrl() + entity.getShortCode());
        newURLResponseDTO.setCurrentRequests(entity.getCurrentRequests());
        newURLResponseDTO.setExpiresAt(entity.getExpiresAt());

        return newURLResponseDTO;
    }

    private Instant expiresAt;
    private String shortURL;
    private Integer currentRequests;
}
