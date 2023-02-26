package com.gpoalelungi.licenta.resource;

import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.service.UserService;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public Response signup(@RequestBody User user) {
        User savedUser = userService.signup(user);
        return Response.ok(savedUser).build();
    }
}
