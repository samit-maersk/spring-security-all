package net.samitkumar.ssa;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String home() {
        // we can get the authentication object from the context like this and make use of it in the page
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "index";
    }
}
