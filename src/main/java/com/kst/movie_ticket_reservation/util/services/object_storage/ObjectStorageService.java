package com.kst.movie_ticket_reservation.util.services.object_storage;

import com.kst.movie_ticket_reservation.util.exceptions.CustomS3Exception;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;

public interface ObjectStorageService
{
    String upload(String folder, MultipartFile file) throws CustomS3Exception;

    void delete(String url) throws URISyntaxException, CustomS3Exception;
}
