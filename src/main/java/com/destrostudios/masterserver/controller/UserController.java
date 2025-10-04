package com.destrostudios.masterserver.controller;

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
    public void register(@RequestBody RegistrationDto registrationDto) throws BadRequestException, LoginAlreadyExistsException, EmailAlreadyExistsException, EmailNotSentException {
        userService.register(registrationDto);
    }

    @PostMapping("/{login}/sendEmailConfirmationEmail")
    public void sendEmailConfirmationEmail(@PathVariable String login) throws UserNotFoundException, TooManyEmailRequestsException, EmailNotSentException {
        userService.sendEmailConfirmationEmail(login);
    }

    @PostMapping("/{login}/confirmEmail")
    public void confirmEmail(@PathVariable String login, @RequestBody String emailSecret) throws UserNotFoundException, WrongEmailSecretException {
        userService.confirmEmail(login, emailSecret);
    }

    @PostMapping("/{login}/sendPasswordResetEmail")
    public void sendPasswordResetEmail(@PathVariable String login) throws UserNotFoundException, TooManyEmailRequestsException, EmailNotSentException {
        userService.sendPasswordResetEmail(login);
    }

    @PostMapping("/{login}/resetPassword")
    public void resetPassword(@PathVariable String login, @RequestBody ResetPasswordDto resetPasswordDto) throws UserNotFoundException, WrongEmailSecretException {
        userService.resetPassword(login, resetPasswordDto.getEmailSecret(), resetPasswordDto.getClientHashedPassword());
    }

    @GetMapping("/{login}/saltClient")
    public String getSaltClient(@PathVariable String login) throws UserNotFoundException {
        return userService.getSaltClient(login);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) throws UserNotFoundException, WrongPasswordException, EmailNotConfirmedException {
        return userService.login(loginDto);
    }

    @GetMapping("/{userIdOrLogin}")
    public UserDetailedDto getUser(@PathVariable String userIdOrLogin) throws UserNotFoundException {
        return userDtoMapper.mapDetailed(userService.getUserByIdOrLogin(userIdOrLogin));
    }
}
