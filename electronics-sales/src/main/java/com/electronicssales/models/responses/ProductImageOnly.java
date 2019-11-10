package com.electronicssales.models.responses;

import java.util.List;

import com.electronicssales.entities.Image;

public interface ProductImageOnly {

    List<Image> getImages();
    
}