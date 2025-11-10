package com.shorturl.shorturl.controllers;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shorturl.shorturl.entities.ShortURLEntity;
import com.shorturl.shorturl.services.ShortURLService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/open")
public class URLRedirectController {

    @Autowired private ShortURLService shortURLService;

    Logger logger = Logger.getLogger(URLRedirectController.class.getName());
    
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectURL(
        @PathVariable String shortCode, 
        HttpServletResponse response
    ) {
        ShortURLEntity shortURLEntity = shortURLService.getUrlByShortCode(shortCode).orElse(null);

        if (shortURLEntity != null){
            try {
                if(shortURLEntity.getCurrentRequests() + 1 > shortURLEntity.getMaxRequests()){
                    logger.info("Short URL with code " + shortCode + " has reached its limits. Deleting link...");
                    shortURLService.deleteShortURLEntiry(shortURLEntity);
                    throw new Exception("Link has reached its limits");
                } else {
                    shortURLEntity.setCurrentRequests(shortURLEntity.getCurrentRequests() + 1);
                    shortURLService.updateShortURLEntity(shortURLEntity);
                    response.sendRedirect(shortURLEntity.getLongURL());
                    return ResponseEntity.status(HttpStatus.FOUND).build();
                }
            } catch (Exception e) {
                logger.info("Failed to redirec");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}