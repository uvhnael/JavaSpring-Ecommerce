package org.uvhnael.ecomapi.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/uploads")
public class UploadController {
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/video")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file type
            String fileType = file.getContentType();
            assert fileType != null;
            if (!fileType.startsWith("video/")) {
                return ResponseEntity.badRequest().body("Only videos are allowed.");
            }

            // Save the file
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            Path path = Paths.get(UPLOAD_DIR + date + "_" + file.getOriginalFilename());
            Files.createDirectories(path.getParent()); // Create the directory if it doesn't exist
            file.transferTo(path);

            return ResponseEntity.ok(path.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/images")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<String> imagePaths = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                // Validate file type
                String fileType = file.getContentType();
                assert fileType != null;
                if (!fileType.startsWith("image/")) {
                    return ResponseEntity.badRequest().body("Only images are allowed.");
                }


                String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                Path path = Paths.get(UPLOAD_DIR + date + "_" + file.getOriginalFilename());
                Files.createDirectories(path.getParent()); // Create the directory if it doesn't exist
                file.transferTo(path);

                imagePaths.add(path.toString());
            }
            return ResponseEntity.status(HttpStatus.OK).body(imagePaths);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("{filename}")
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            Path path = Paths.get(UPLOAD_DIR + filename);
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }
//            return file
            byte[] readAllBytes = Files.readAllBytes(path);
            return ResponseEntity.ok(readAllBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File retrieval failed: " + e.getMessage());
        }
    }


}
