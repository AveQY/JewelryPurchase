package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.Dao.ProductImgRepository;
import com.aweqy.jewelrypurchaseweb.jpw.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PictureFileController {

    @Value("${app.upload-dir}")
    private String UPLOAD_DIR; // 服务器存储路径

    @Autowired
    private ProductImgRepository productImgRepository;

    @PostMapping("/picture_upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("orderNumber") String orderNumber) {

        // 校验文件是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件不能为空");
        }

        try {
            // 生成唯一文件名（防重复）
            String fileName = UUID.randomUUID() + ".png";
            Path path = Paths.get(UPLOAD_DIR + fileName);

            // 保存文件到本地
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 将文件路径存入数据库
            InetAddress inetAddress = InetAddress.getLocalHost();
            String urlName = inetAddress.getHostAddress() + ":6601";
            System.out.println(urlName);
            if (inetAddress.getHostAddress().equals("111.231.70.140")) {
                urlName = "aweqy.asia/JewelryPurchase";
            }
            String fileUrl = "http://" + urlName + "/api/image/" + fileName; // 访问URL

            productImgRepository.save(new ProductImage(Integer.valueOf(orderNumber), fileUrl));

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("上传失败");
        }
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {
        Path imagePath = Paths.get(UPLOAD_DIR + filename);
        byte[] imageBytes = Files.readAllBytes(imagePath);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 根据实际类型调整
                .body(imageBytes);
    }

    @GetMapping("/search/productImg")
    public List<ProductImage> registerUser(@RequestParam int id) {
        List<ProductImage> img = productImgRepository.searchProductImageById(id);
        if (img != null) {
            return img;
        }
        return null;
    }

}
