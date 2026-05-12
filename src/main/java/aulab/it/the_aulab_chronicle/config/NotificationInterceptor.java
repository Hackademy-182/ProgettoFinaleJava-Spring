package aulab.it.the_aulab_chronicle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import aulab.it.the_aulab_chronicle.repositories.ArticleRepository;
import aulab.it.the_aulab_chronicle.repositories.CareerRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NotificationInterceptor implements HandlerInterceptor {

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private ArticleRepository articleRepository;

   @Override
public void postHandle(HttpServletRequest request,
                       HttpServletResponse response,
                       Object handler,
                       ModelAndView modelAndView) {

    System.out.println(">>> INTERCEPTOR ATTIVO");
    System.out.println("URI = " + request.getRequestURI());

    if (modelAndView == null) {
        System.out.println(">>> MODELANDVIEW NULL");
        return;
    }

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    System.out.println(">>> RUOLI UTENTE:");
    auth.getAuthorities().forEach(a ->
            System.out.println("AUTH: " + a.getAuthority())
    );

    boolean isRevisor = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_REVISOR"));

    boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));

    System.out.println(">>> isRevisor = " + isRevisor);
    System.out.println(">>> isAdmin = " + isAdmin);

    if (isAdmin) {
        int careerCount = careerRequestRepository.findByIsCheckedFalse().size();
        System.out.println(">>> CAREER REQUESTS = " + careerCount);

        modelAndView.addObject("careerRequests", careerCount);
    }

    if (isRevisor) {
        int revisedCount = articleRepository.findByIsAcceptedNull().size();
        System.out.println(">>> ARTICLES TO REVIEW = " + revisedCount);

        modelAndView.addObject("articlesToBeRevised", revisedCount);
    }
}
}