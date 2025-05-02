package com.foodon.foodon.image.domain;

import com.foodon.foodon.image.exception.ImageErrorCode;
import com.foodon.foodon.image.exception.ImageException;
import com.foodon.foodon.image.exception.ImageException.ImageBadRequestException;
import lombok.Getter;

import java.util.Arrays;

import static com.foodon.foodon.image.exception.ImageErrorCode.ILLEGAL_IMAGE_EXTENSION;

@Getter
public enum ImageExtension {

    JPEG(".jpeg", "ffd8ffe"),
    JPG(".jpg", "ffd8ffe"),
    JFIF(".jfif", "ffd8ffe"),
    PNG(".png", "89504e47"),
    ;

    private final String extension;
    private final String signature;

    ImageExtension(String extension, String signature) {
        this.extension = extension;
        this.signature = signature;
    }

    public static ImageExtension from(String imageFileName) {
        return Arrays.stream(values())
                .filter(imageExtension -> imageFileName.endsWith(imageExtension.getExtension()))
                .findFirst()
                .orElseThrow(() -> new ImageBadRequestException(ILLEGAL_IMAGE_EXTENSION));
    }

}
