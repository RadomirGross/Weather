    package com.gross.weather.controllers;

    import com.gross.weather.model.Session;
    import com.gross.weather.model.User;
    import com.gross.weather.service.LocationService;
    import com.gross.weather.service.SessionService;
    import com.gross.weather.service.UserService;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.ResponseBody;

    import java.util.ArrayList;
    import java.util.List;


    @Controller
    public class AuthController {
        private final UserService userService;
        private final SessionService sessionService;

        @Autowired
        public AuthController(UserService userService, SessionService sessionService) {
            this.userService = userService;
            this.sessionService = sessionService;
        }


        @GetMapping("/sign-in")
        public String signIn() {
            return "sign-in";
        }



        @PostMapping("/sign-in")
        public String signIn(@RequestParam("username") String username, @RequestParam("password") String password,
                             Model model, HttpServletResponse response) {
            List<String> errors = new ArrayList<>();
            User user = userService.findUserByLogin(username);
            if (user == null) {
                errors.add("username not found, please try register");
                model.addAttribute("errors", errors);
                return "sign-in";
            } else if (!password.equals(user.getPassword())) {
                errors.add("passwords do not match");
                model.addAttribute("errors", errors);
                return "sign-in";
            } else {

                Session session=sessionService.saveSession(new Session(user.getId()));
                Cookie cookie = new Cookie("SESSION", session.getId().toString());
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24);
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                response.addCookie(cookie);
                return "redirect:/";
            }
        }
    }
