package aulab.it.the_aulab_chronicle.services;

import java.util.UUID;

import aulab.it.the_aulab_chronicle.models.Article;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import aulab.it.the_aulab_chronicle.models.Image;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    @Value("${supabase.image}")
    private String supabaseImage;

    private final RestTemplate restTemplate = new RestTemplate();

    void saveImageOnDB(String url, Article article){
        url = url.replace(supabaseBucket, supabaseImage);
        imageRepository.save(Image.builder().path(url).article(article).build());
    }

    @Async
    CompletableFuture<String> saveImageOnnCloud(MultipartFile file) throws Exception{
        if (!file.isEmpty()) {
            try {
                String newFile = UUID.randomUUID().toString() + " " + file.getOriginalFilename();
                String extention = StringManipulation.getFileExtention(nameFile);
                String url = supabaseUrl + supabaseBucket + nameFile;

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("file", file.getBytes());

                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-type", "/image" + extention);
                headers.set("Authorization", "Bearer" + supabaseKey);

                HttpEntity<byte[]> requesEntity = new HttpEntity<>(file.getBytes(), headers);
                restTemplate.exchange(url, HttpMethod.POST, requesEntity, String.class);

                return CompletableFuture.completedFuture(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            throw new IllegalArgumentException("Non hai inserito un'immagine");
        }
        return CompletableFuture.failedFuture(null);
    }

    void deleteImage(String imagePath) throws IDException{

    }
}
