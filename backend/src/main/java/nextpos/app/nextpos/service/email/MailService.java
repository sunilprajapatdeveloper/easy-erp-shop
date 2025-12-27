package nextpos.app.nextpos.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML

        mailSender.send(message);
    }

    public void sendPlainTextEmail(String to, String subject, String textContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false); // false = plain
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textContent, false);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send plain text email", e);
        }
    }

    public String buildPasswordEmail(String password) {
        return """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 8px; background-color: #f9f9f9;">
                        <div style="text-align: center; padding-bottom: 20px;">
                            <h2 style="color: #2c3e50;">Welcome to <span style="color: #4CAF50;">NextPOS</span></h2>
                        </div>
                        <p style="font-size: 16px; color: #333;">Hi there,</p>
                        <p style="font-size: 16px; color: #333;">
                            Your account has been created successfully. Use the temporary password below to log in:
                        </p>
                        <div style="padding: 15px; background-color: #ffffff; border: 1px solid #ddd; border-radius: 4px; text-align: center; margin: 20px 0;">
                            <span style="font-size: 18px; font-weight: bold; color: #4CAF50;">%s</span>
                        </div>
                        <p style="font-size: 16px; color: #333;">
                            For security reasons, please make sure to change your password after logging in.
                        </p>
                        <p style="font-size: 14px; color: #999;">If you didn’t request this account, you can ignore this email.</p>
                        <hr style="border: none; border-top: 1px solid #eee;" />
                        <p style="font-size: 12px; color: #aaa; text-align: center;">
                            &copy; %d NextPOS. All rights reserved.
                        </p>
                    </div>
                """
                .formatted(password, java.time.LocalDate.now().getYear());
    }
}
