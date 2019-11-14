package com.electronicssales.services;

import com.electronicssales.entities.Image;
import com.electronicssales.models.responses.ImageDataResponse;

public interface ImageService {

    Image saveImage(Image image);

    Image saveImage(byte[] imageData);

    ImageDataResponse getImageDataById(long id);
    
}