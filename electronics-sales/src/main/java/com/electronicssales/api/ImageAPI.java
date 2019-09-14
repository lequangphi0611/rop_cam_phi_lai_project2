package com.electronicssales.api;

import java.io.IOException;

import com.electronicssales.entities.Image;
import com.electronicssales.services.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageAPI {

    @Autowired
    ImageService imageService;

    @PostMapping
    public ResponseEntity<Image> saveImage(@RequestBody MultipartFile file) throws IOException {
        Image imageSaved = imageService.saveImage(file.getBytes());
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(imageSaved, status);
    }
    
    @GetMapping(
        value = "/{id}",
        produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] fetchImage(@PathVariable long id) {
        return imageService.getImageDataById(id);
    }
}