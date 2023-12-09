package com.destrostudios.masterserver.service;

import com.destrostudios.masterserver.database.UserRepository;
import com.destrostudios.masterserver.database.schema.User;
import com.destrostudios.masterserver.model.LoginDto;
import com.destrostudios.masterserver.model.RegistrationDto;
import com.destrostudios.masterserver.service.exceptions.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private EmailService emailService;

    @Transactional
    public void register(RegistrationDto registrationDto) throws BadRequestException, LoginAlreadyExistsException, EmailAlreadyExistsException {
        if (isEmpty(registrationDto.getLogin()) || isEmpty(registrationDto.getEmail()) || isEmpty(registrationDto.getSaltClient()) || isEmpty(registrationDto.getClientHashedPassword())) {
            throw new BadRequestException();
        }
        Optional<User> loginUser = userRepository.findByLogin(registrationDto.getLogin());
        if (loginUser.isPresent()) {
            throw new LoginAlreadyExistsException();
        }
        Optional<User> emailUser = userRepository.findByEmail(registrationDto.getEmail());
        if (emailUser.isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        String saltServer = generateSalt();
        String hashedPassword = hashSecret(registrationDto.getClientHashedPassword(), saltServer);
        User user = User.builder()
            .login(registrationDto.getLogin())
            .email(registrationDto.getEmail())
            .emailSecret(generateUUID())
            .saltClient(registrationDto.getSaltClient())
            .saltServer(saltServer)
            .hashedPassword(hashedPassword)
            .build();
        userRepository.save(user);
        sendEmailConfirmationEmail(user);
    }

    public void sendEmailConfirmationEmail(String login) throws UserNotFoundException {
        User user = getUserByLogin(login);
        sendEmailConfirmationEmail(user);
    }

    private void sendEmailConfirmationEmail(User user) {
        sendMail(user, "Confirm your e-mail address", "Here is the code to confirm your e-mail address: " + user.getEmailSecret());
    }

    public void sendPasswordResetEmail(String login) throws UserNotFoundException {
        User user = getUserByLogin(login);
        sendMail(user, "Reset your password", "Here is the code to reset your password: " + user.getEmailSecret());
    }

    private void sendMail(User user, String subject, String text) {
        emailService.sendEmail(user.getEmail(), subject, "Hello " + user.getLogin() + "! " + text);
    }

    @Transactional
    public void confirmEmail(int userId, String emailSecret) throws UserNotFoundException, WrongEmailSecretException {
        updateUserViaEmailSecret(userId, emailSecret, user -> user.setEmailConfirmed(true));
    }

    @Transactional
    public void resetPassword(int userId, String emailSecret, String clientHashedPassword) throws UserNotFoundException, WrongEmailSecretException {
        updateUserViaEmailSecret(userId, emailSecret, user -> {
            String hashedPassword = hashSecret(clientHashedPassword, user.getSaltServer());
            user.setHashedPassword(hashedPassword);
        });
    }

    private void updateUserViaEmailSecret(int userId, String emailSecret, Consumer<User> updateUser) throws UserNotFoundException, WrongEmailSecretException {
        User user = getUserById(userId);
        if (!emailSecret.equals(user.getEmailSecret())) {
            throw new WrongEmailSecretException();
        }
        user.setEmailSecret(generateUUID());
        updateUser.accept(user);
        userRepository.save(user);
    }

    public String getSaltClient(String login) throws UserNotFoundException {
        return userRepository.findSaltClientByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    public String login(LoginDto loginDto) throws UserNotFoundException, WrongPasswordException, EmailNotConfirmedException {
        User user = getUserByLogin(loginDto.getLogin());
        String hashedPassword = hashSecret(loginDto.getClientHashedPassword(), user.getSaltServer());
        if (!hashedPassword.equals(user.getHashedPassword())) {
            throw new WrongPasswordException();
        }
        if (!user.isEmailConfirmed()) {
            throw new EmailNotConfirmedException();
        }
        return authTokenService.signToken(user);
    }

    public User getUserByIdOrLogin(String idOrLogin) throws UserNotFoundException {
        try {
            int id = Integer.parseInt(idOrLogin);
            return getUserById(id);
        } catch (NumberFormatException ex) {
            return getUserByLogin(idOrLogin);
        }
    }

    public User getUserById(int id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User getUserByLogin(String login) throws UserNotFoundException {
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private String generateSalt() {
        return BCrypt.gensalt(10);
    }

    private String hashSecret(String secret, String salt) {
        return BCrypt.hashpw(secret, salt);
    }
}
