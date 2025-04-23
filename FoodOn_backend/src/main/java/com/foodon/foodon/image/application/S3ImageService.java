package com.foodon.foodon.image.application;

import com.foodon.foodon.image.domain.ImageExtension;
import com.foodon.foodon.image.domain.S3Client;
import com.foodon.foodon.image.domain.UploadFile;
import com.foodon.foodon.image.exception.ImageException.ImageBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.foodon.foodon.image.exception.ImageErrorCode.ILLEGAL_IMAGE_FORMAT;
import static com.foodon.foodon.image.exception.ImageErrorCode.IMAGE_IS_NULL;

@Service
@RequiredArgsConstructor
public class S3ImageService implements ImageService {

    @Value("${cloud.aws.s3.upload-path}")
    private String uploadPath;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    private final S3Client s3Client;

    @Override
    public String upload(MultipartFile multipartFile) {

        validateImageFileFormat(multipartFile);

        try {
            UploadFile uploadFile = UploadFile.from(multipartFile);
            s3Client.upload(uploadFile);
            return getImageUrl(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateImageFileFormat(MultipartFile multipartFile) {
        if(multipartFile == null || multipartFile.isEmpty()) {
            throw new ImageBadRequestException(IMAGE_IS_NULL);
        }

        if(!isValidImage(multipartFile)) {
            throw new ImageBadRequestException(ILLEGAL_IMAGE_FORMAT);
        }
    }

    /*
        파일 확장자 변조 방지하기 위한 이미지 파일 유효성 체크
     */
    private boolean isValidImage(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] headerBytes = new byte[4];
            if (inputStream.read(headerBytes) == -1) return false;

            String hex = bytesToHex(headerBytes);
            return hex.startsWith(getSignature(multipartFile.getOriginalFilename()));
        } catch (IOException e) {
            return false;
        }
    }

    private String getSignature(String origFileName) {
        return ImageExtension.from(origFileName).getSignature();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString().toLowerCase();
    }

    private String getImageUrl(UploadFile uploadFile) {
        return baseUrl + String.join(
                "/",
                uploadPath,
                uploadFile.getOriginalFilename()
        );
    }

}
