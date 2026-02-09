package nextpos.app.nextpos.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Retryable(value = { MailException.class,
            MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        log.debug("Sending HTML email to: {}, subject: {}", to, subject);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("noreply@gmail.com");

        mailSender.send(message);
        log.info("HTML email sent successfully to: {}", to);
    }

    @Retryable(value = { MailException.class,
            MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void sendPlainTextEmail(String to, String subject, String textContent) throws MessagingException {
        log.debug("Sending plain text email to: {}, subject: {}", to, subject);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(textContent);
        helper.setFrom("noreply@gmail.com");

        mailSender.send(message);
        log.info("Plain text email sent successfully to: {}", to);
    }

    public String buildPasswordEmail(String password) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Your Account Password</title>
                            <style>
                                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                                .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                                         color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                                .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                                .password { background: white; padding: 20px; margin: 20px 0;
                                           border-radius: 5px; border-left: 4px solid #667eea;
                                           font-family: monospace; font-size: 18px; font-weight: bold;
                                           text-align: center; }
                                .warning { background: #fff3cd; border: 1px solid #ffeaa7;
                                          padding: 15px; border-radius: 5px; margin: 20px 0; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>Welcome to NextPOS!</h1>
                                </div>
                                <div class="content">
                                    <h2>Your Account has been Created</h2>
                                    <p>Hi there,</p>
                                    <p>Your account has been successfully created. Please use the temporary password below to log in:</p>

                                    <div class="password">%s</div>

                                    <div class="warning">
                                        <strong>Security Notice:</strong>
                                        <ul>
                                            <li>This is a temporary password</li>
                                            <li>You must change your password after first login</li>
                                            <li>Do not share this password with anyone</li>
                                        </ul>
                                    </div>

                                    <p>To get started:</p>
                                    <ol>
                                        <li>Go to the login page</li>
                                        <li>Enter your email address and the temporary password above</li>
                                        <li>Follow the prompts to set a new secure password</li>
                                    </ol>

                                    <p>If you have any questions or need assistance, please contact our support team.</p>

                                    <p>Best regards,<br>The NextPOS Team</p>
                                </div>
                                <div class="footer">
                                    <p>© %d NextPOS. All rights reserved.</p>
                                    <p>This is an automated message, please do not reply to this email.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                password,
                java.time.Year.now().getValue());
    }
}
