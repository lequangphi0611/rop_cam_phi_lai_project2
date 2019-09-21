package com.electronicssales.resources;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/images")
public class ImageResource {

    @Autowired
    ImageService imageService;

    @GetMapping(
        value = "/{id}",
        produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] fetchImage(@PathVariable long id) {
        return imageService.getImageDataById(id);
    }

    @PostMapping
    public ResponseEntity<Image> saveImage(@RequestBody MultipartFile file) throws IOException {
        Image imageSaved = imageService.saveImage(file.getBytes());
        URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/api/images/{id}")
            .buildAndExpand(imageSaved.getId())
            .toUri();
        return ResponseEntity
            .created(location)
            .body(imageSaved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Collection<Image>> saveImages(@RequestBody MultipartFile[] files) throws IOException {
        Collection<Image> images = Arrays.asList(files)
            .stream()
            .map((file) -> {
                    try {
                        return imageService.saveImage(file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
            .collect(Collectors.toList());

        return new ResponseEntity<Collection<Image>>(images, HttpStatus.CREATED);
    }

}