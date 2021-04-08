package project.recipemanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import project.recipemanager.model.ActivationCode;
import project.recipemanager.model.User;
import project.recipemanager.repository.ActivationCodeRepository;
import project.recipemanager.repository.UserRepository;

import java.util.UUID;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ActivationCodeRepository codeRepo;

    @Autowired
    private UserRepository userRepo;

    private static String URL_ROOT = "http://localhost:4200";
    private static String SUBJECT = "Manager Przepisów - Link aktywacyjny";

    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendActivationEmailToUser(User userToActivate) {
        String userEmail = userToActivate.getEmail();
        String code = UUID.randomUUID().toString();
        ActivationCode activationCode = new ActivationCode(code);

        activationCode.setUser(userToActivate);
        activationCode = codeRepo.save(activationCode);

        userToActivate.setActivationCode(activationCode);
        userRepo.save(userToActivate);

        String mailContent = "Kliknij w link, aby aktywować swoje konto w Managerze Przepisów: \n";
        mailContent += URL_ROOT + "/" + activationCode.getId() + "/" + code;
        sendSimpleMessage(userEmail, SUBJECT, mailContent);
    }
}
