package com.javvathehutt.hic_gateway.authentication;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final String COOKIE_NAME = "hic_jwt_auth";
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/register")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/auth/authenticate")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(@AuthenticationPrincipal
                                                           @RequestBody AuthenticationRequest request, HttpServletResponse responseServlet
    ) {

        AuthenticationResponse response = authenticationService.authenticate(request);

        Cookie cookie = new Cookie(COOKIE_NAME, response.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(600);
        cookie.setPath("/");
        cookie.setAttribute("Expires", LocalDateTime.now().toString());

        responseServlet.addCookie(cookie);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/")
    public String getAuthHtml() {
        return "auth";
    }

    @GetMapping(value = "/registration")
    public String getRegisterHtml() {
        return "register";
    }
}
