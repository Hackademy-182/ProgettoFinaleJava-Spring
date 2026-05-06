package aulab.it.the_aulab_chronicle.services;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public interface CrudService<ReadDto, Model, Key> {

    List<ReadDto> readAll();
    ReadDto read(Key key);
    ReadDto create(Model model, Principal principal, MultipartFile file);
    ReadDto update(Model model, Principal principal, MultipartFile file);
    void delete(Key key);
    
}
