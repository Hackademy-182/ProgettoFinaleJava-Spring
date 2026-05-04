package aulab.it.the_aulab_chronicle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import aulab.it.the_aulab_chronicle.services.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
}
