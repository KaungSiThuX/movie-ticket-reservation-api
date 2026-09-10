package com.kst.movie_ticket_reservation.util.services.object_storage.impl;

import com.kst.movie_ticket_reservation.integration.cloudflare_r2.service.CloudflareR2Service;
import com.kst.movie_ticket_reservation.util.exceptions.CustomS3Exception;
import com.kst.movie_ticket_reservation.util.services.object_storage.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class ObjectStorageServiceImpl implements ObjectStorageService
{
    private final CloudflareR2Service cloudflareR2Service;

    @Override
    public String upload(String folder, MultipartFile file) throws CustomS3Exception
    {
        return this.cloudflareR2Service.uploadObject(folder, file);
    }

    @Override
    public void delete(String url) throws URISyntaxException, CustomS3Exception
    {
        this.cloudflareR2Service.deleteObject(url);
    }
}
