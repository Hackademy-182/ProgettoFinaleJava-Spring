package aulab.it.the_aulab_chronicle.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aulab.it.the_aulab_chronicle.dtos.CategoryDto;
import aulab.it.the_aulab_chronicle.models.Category;
import aulab.it.the_aulab_chronicle.repositories.CategoryRepository;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CategoryDto update(Category model, Principal principal, MultipartFile file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Long key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
