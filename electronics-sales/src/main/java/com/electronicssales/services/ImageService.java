package com.electronicssales.services;

import com.electronicssales.entities.Image;

public interface ImageService {

    Image saveImage(Image image);

    Image saveImage(byte[] imageData);

    byte[] getImageDataById(long id);
    
}