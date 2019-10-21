package com.electronicssales.models;

import com.electronicssales.models.responses.IParagraphResponse;
import com.electronicssales.models.responses.ParagraphResponse;
import com.electronicssales.utils.Mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ParagraphResponseMapper implements Mapper<ParagraphResponse, IParagraphResponse> {

    @Override
    public ParagraphResponse mapping(IParagraphResponse iParagraphResponse) {
        ParagraphResponse paragraphResponse = new ParagraphResponse();
        paragraphResponse.setId(iParagraphResponse.getId());
        paragraphResponse.setImageId(iParagraphResponse.getImageId());
        paragraphResponse.setText(iParagraphResponse.getText());
        paragraphResponse.setTitle(iParagraphResponse.getTitle());
        return paragraphResponse;
    }
    
}