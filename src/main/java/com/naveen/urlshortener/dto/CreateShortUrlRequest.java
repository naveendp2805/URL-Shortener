package com.naveen.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateShortUrlRequest {

    @NotBlank(message = "URL cannot be empty")
    @Pattern(regexp = "^(https?://).+", message = "URL must start with http:// or https://")
    private String longUrl;
}
