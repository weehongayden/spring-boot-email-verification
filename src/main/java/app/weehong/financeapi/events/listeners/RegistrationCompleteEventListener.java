package app.weehong.financeapi.events.listeners;

import app.weehong.financeapi.dtos.requests.UserRequestDto;
import app.weehong.financeapi.events.RegistrationCompleteEvent;
import app.weehong.financeapi.services.AuthenticationServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final AuthenticationServiceImpl authenticationService;

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private UserRequestDto userRequestDto;

    @Autowired
    public RegistrationCompleteEventListener(AuthenticationServiceImpl authenticationService, JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.authenticationService = authenticationService;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        userRequestDto = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        authenticationService.createVerificationToken(userRequestDto, verificationToken);
        String url = event.getApplicationUrl() + "/authentication/verify?token=" + verificationToken;

        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Action Required: Verify Your Email for Account Activation";
        String username = "Finance App";

        Context context = new Context();
        context.setVariable("name", userRequestDto.name());
        context.setVariable("url", url);

        String content = templateEngine.process("email/verification", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setFrom("noreply@weehong.app", username);
        messageHelper.setTo(userRequestDto.email());
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);

        mailSender.send(message);
    }
}
