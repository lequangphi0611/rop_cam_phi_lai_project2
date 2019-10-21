package com.electronicssales.models.responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParagraphResponse {

    long id;

    String text;

    String title;

    Long imageId;
    
}