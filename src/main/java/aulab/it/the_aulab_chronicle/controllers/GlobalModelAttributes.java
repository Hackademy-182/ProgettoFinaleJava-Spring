package aulab.it.the_aulab_chronicle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import aulab.it.the_aulab_chronicle.repositories.ArticleRepository;
import aulab.it.the_aulab_chronicle.repositories.CareerRequestRepository;

@ControllerAdvice
public class GlobalModelAttributes {

     @Autowired
    private CareerRequestRepository careerRequestRepository;

    @ModelAttribute("careerRequests")
    public Long unreadRequests() {
        return careerRequestRepository.countByIsCheckedFalse();
    }

     @Autowired
    private ArticleRepository articleRepository;

    @ModelAttribute("articlesToBeRevised")
    public long articlesToBeRevised() {
        return articleRepository.countByIsAcceptedNull();
    }
}
