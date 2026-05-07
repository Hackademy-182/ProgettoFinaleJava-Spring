package aulab.it.the_aulab_chronicle.services;

import aulab.it.the_aulab_chronicle.models.Article;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.multipart.MultipartFile;
import java.io.IDException;


public interface ImageService {
    void saveImageOnDB(String url, Article article);
    CompletableFuture<String> saveImageOnnCloud(MultipartFile file) throws Exception;
    void deleteImage(String imagePath) throws IDException;
}
