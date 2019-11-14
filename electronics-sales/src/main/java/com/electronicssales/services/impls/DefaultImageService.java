package com.electronicssales.services.impls;

import java.util.Optional;

import com.electronicssales.entities.Image;
import com.electronicssales.models.responses.ImageDataResponse;
import com.electronicssales.repositories.ImageRepository;
import com.electronicssales.services.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class DefaultImageService implements ImageService {

    @Lazy
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image saveImage(Image image) {
        Image result = new Image();
        result.setId(imageRepository
                    .save(image)
                    .getId());

        return result;
    }

    @Override
    public Image saveImage(byte[] imageData) {
        return Optional.of(imageData)
            .map(Image::of)
            .map(this::saveImage)
            .get();
    }

    @Override
    public ImageDataResponse getImageDataById(long id) {
        return imageRepository
        .findById(id)
        .map(image -> image.getData())
        .map(ImageDataResponse::new)
        .get();
    }

    
}