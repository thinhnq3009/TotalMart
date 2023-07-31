package eco.mart.totalmart.components;

import eco.mart.totalmart.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GmailSenderService {

    private static final String FILE_NAME = "/email-template.html";
    private final Logger logger = LoggerFactory.getLogger(GmailSenderService.class);

    private final JavaMailSender javaMailSender;

    private final ServletContext context;

    private final HttpServletRequest request;

    private final String SYNTAX = "<@>";


    public String getCurrentDomain() {
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        return url.substring(0, url.indexOf(uri));
    }

    @Async
    public void sendMail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("thinhnq3009@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void sendResetPasswordMail(User user) throws MessagingException, FileNotFoundException {


        Map<String, String> map = new HashMap<>();


        map.put("username", user.getUsername());
        map.put("resetLink", getCurrentDomain() + "/reset-password/" + user.getResetPasswordToken());
        map.put("rootLink", getCurrentDomain());
        map.put("time", "Util 12:33 11/12/2023");

        sendMail(user.getEmail(), user.getFullname().formatted("Reset password for %s"), map);
    }

    public void sendMail(String to, String subject, Map<String, String> map) throws MessagingException, FileNotFoundException {
        String body = null;
        try {
            body = getEmailBody(map);
        } catch (FileNotFoundException e) {
            body = getCurrentDomain() + "/reset-password/" + map.get("resetPasswordToken");
            throw e;
        } finally {
            sendMail(to, subject, body);
        }
    }

    private String getEmailBody(Map<String, String> map) throws FileNotFoundException {
        String pathString = context.getRealPath(FILE_NAME);
        Path path = Path.of(pathString);
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .map(line -> replaceSyntax(line, map))
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            logger.error("File not found: " + pathString, e);
            throw new FileNotFoundException("File not found: " + pathString);
        }
    }

    private String replaceSyntax(String line, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String syntax = SYNTAX + entry.getKey() + SYNTAX;
            line = line.replace(syntax, entry.getValue());
        }
        return line;
    }


}
