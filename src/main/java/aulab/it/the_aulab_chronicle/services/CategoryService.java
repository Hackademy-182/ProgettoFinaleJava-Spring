package aulab.it.the_aulab_chronicle.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import aulab.it.the_aulab_chronicle.dtos.CategoryDto;
import aulab.it.the_aulab_chronicle.models.Article;
import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.repositories.CategoryRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoryService implements CrudService<CategoryDto, Category, Long> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryDto> readAll() {
        List<CategoryDto> dtos = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            dtos.add(modelMapper.map(category, CategoryDto.class));
        }
        return dtos;
    }

    @Override
    public CategoryDto read(Long key) {
        return modelMapper.map(categoryRepository.findById(key), CategoryDto.class);
        
    }

    @Override
    public CategoryDto create(Category model, Principal principal, MultipartFile file) {
        
        return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
    }

    @Override
    public CategoryDto update(Long key, Category updatedCategory, MultipartFile file) {

        Category category = categoryRepository.findById(key)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        category.setName(updatedCategory.getName());

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
       
        if (categoryRepository.existsById(id)) {
            Category category = categoryRepository.findById(id).get();

            if (category.getArticles() != null) {
                Iterable<Article> articles = category.getArticles();
                for (Article article : articles) {
                    article.setCategory(null);
                }
            }
            categoryRepository.deleteById(id);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


    
}
