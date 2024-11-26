package org.uvhnael.ecomapi.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Model.Gallery;
import org.uvhnael.ecomapi.Repository.GalleryRepository;
import org.uvhnael.ecomapi.exception.gallery.GalleryCreationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;

    public List<Gallery> getByProductId(int productId) {
        return galleryRepository.getByProductId(productId);
    }

    public String getThumbnail(int productId) {
        return galleryRepository.getThumbnail(productId);
    }

    public List<String> getThumbnails(List<Integer> ids) {
        return galleryRepository.getThumbnailList(ids);
    }

    public void createGallery(int productId, List<String> imagePath, int createdBy) {
        for (int i = 0; i < imagePath.size(); i++) {
            if (!galleryRepository.createGallery(productId, imagePath.get(i), i == 0, i, createdBy)) {
                throw new GalleryCreationException("Gallery creation failed");
            }
        }
    }
}
