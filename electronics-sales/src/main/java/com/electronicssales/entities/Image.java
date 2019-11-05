package com.electronicssales.entities;

import java.io.IOException;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @JsonIgnore
    private byte[] data;

    public Image(long id) {
        this.id = id;
    }

    public static Image of(byte[] data) {
        Image image = new Image();
        image.setData(data);
        return image;
    }

    public static Image of(MultipartFile multipartFile) {
        Image image = null;
        if (Optional.ofNullable(multipartFile).isPresent()) {
            try {
                image = Image.of(multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
    
}