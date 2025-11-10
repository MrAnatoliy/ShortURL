package com.shorturl.shorturl.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "short_url")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ShortURLEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String shortCode;

    @Column(nullable = false, length = 2000)
    private String longURL;

    @Column(nullable = false)
    private UUID userUUID;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private int maxRequests;

    @Column(nullable = false)
    private int currentRequests;

    @Column(nullable = false)
    private Instant expiresAt;
}
