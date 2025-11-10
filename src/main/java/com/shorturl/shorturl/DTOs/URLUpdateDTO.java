package com.shorturl.shorturl.DTOs;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class URLUpdateDTO {

    @NotNull
    private UUID userUUID;          // owner key

    @Positive
    private Integer maxRequests;    // optional new value

    @NotNull
    @Positive
    private Long ttlSeconds;        // optional new TTL (seconds)
}