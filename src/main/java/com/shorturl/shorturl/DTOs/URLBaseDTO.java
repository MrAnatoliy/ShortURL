package com.shorturl.shorturl.DTOs;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class URLBaseDTO {
    @NotBlank(message = "Long URL is required")
    @Pattern(
        regexp = "^(https?)://.*$",
        message = "URL must start with http://, https://, or ftp://"
    )
    @Size(max = 2000, message = "URL must not exceed 2000 characters")
    private String longURL;
    private UUID userUUID;
}
