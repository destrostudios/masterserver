package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.database.schema.User;
import com.destrostudios.masterserver.model.*;
import com.destrostudios.masterserver.service.UserService;
import com.destrostudios.masterserver.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationDto registrationDto) throws BadRequestException, LoginAlreadyExistsException, EmailAlreadyExistsException {
        userService.register(registrationDto);
    }

    @PostMapping("/{login}/sendEmailConfirmationEmail")
    public void sendEmailConfirmationEmail(@PathVariable String login) throws UserNotFoundException {
        userService.sendEmailConfirmationEmail(login);
    }

    @PostMapping("/{userId}/confirmEmail")
    public void confirmEmail(@PathVariable int userId, @RequestParam String secret) throws UserNotFoundException, WrongEmailSecretException {
        userService.confirmEmail(userId, secret);
    }

    @PostMapping("/{login}/sendPasswordResetEmail")
    public void sendPasswordResetEmail(@PathVariable String login) throws UserNotFoundException {
        userService.sendPasswordResetEmail(login);
    }

    @PostMapping("/{userId}/resetPassword")
    public void resetPassword(@PathVariable int userId, @RequestBody ResetPasswordDto resetPasswordDto) throws UserNotFoundException, WrongEmailSecretException {
        userService.resetPassword(userId, resetPasswordDto.getEmailSecret(), resetPasswordDto.getClientHashedPassword());
    }

    @GetMapping("/{login}/saltClient")
    public String getSaltClient(@PathVariable String login) throws UserNotFoundException {
        return userService.getSaltClient(login);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) throws UserNotFoundException, WrongPasswordException {
        return userService.login(loginDto);
    }

    @GetMapping("/{userIdOrLogin}")
    public UserDto getUser(@PathVariable String userIdOrLogin) throws UserNotFoundException {
        User user = userService.getUserByIdOrLogin(userIdOrLogin);
        return userDtoMapper.map(user);
    }
}
