package nextpos.app.nextpos.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.enums.VerificationType;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationEmailService {

    private final MailService mailService;

    public void sendVerificationEmail(UUID verificationId, String email, String token,
            VerificationType type, Duration expiryDuration) {
        try {
            String subject = getEmailSubject(type);

            // Check if token is a numeric OTP or a Base64 Secure Token
            String displayedToken = formatTokenBasedOnType(token);

            String htmlContent = buildVerificationEmailHtml(displayedToken, type, expiryDuration);

            mailService.sendHtmlEmail(email, subject, htmlContent);
            log.info("Verification email successfully sent to {} for id: {}", email, verificationId);
        } catch (Exception e) {
            log.error("CRITICAL: Failed to send verification email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    private String formatTokenBasedOnType(String token) {
        if (token == null)
            return "";

        // If it's a numeric OTP (e.g., "123456"), format with spaces for readability
        if (token.matches("\\d+")) {
            return formatNumericOtp(token);
        }

        // If it's a Base64 Secure Token, do NOT change case or add spaces.
        return token;
    }

    private String formatNumericOtp(String token) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ");
            }
            formatted.append(token.charAt(i));
        }
        return formatted.toString();
    }

    private String getEmailSubject(VerificationType type) {
        return switch (type) {
            case USER_REGISTRATION -> "Verify Your Account - EasyErpShop";
            case PASSWORD_RESET -> "Password Reset Request - EasyErpShop";
            case EMAIL_OTP_LOGIN -> "Your Login Code - EasyErpShop";
            case EMAIL_CHANGE_CONFIRMATION -> "Confirm Email Change - EasyErpShop";
            case COMPANY_VERIFICATION -> "Company Verification Required - EasyErpShop";
            case WAREHOUSE_VERIFICATION -> "Warehouse Access Verification - EasyErpShop";
            case TWO_FACTOR_AUTH -> "Two-Factor Authentication Code - EasyErpShop";
            case TRANSACTION_CONFIRMATION -> "Transaction Confirmation - EasyErpShop";
            case DOCUMENT_APPROVAL -> "Document Approval Required - EasyErpShop";
            default -> "Verification Required - EasyErpShop";
        };
    }

    private String buildVerificationEmailHtml(String token, VerificationType type, Duration expiryDuration) {
        String action = getActionDescription(type);
        long expiryMinutes = expiryDuration.toMinutes();
        int currentYear = LocalDateTime.now().getYear();

        return String.format(
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Verification Required</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    line-height: 1.6;
                                    color: #333;
                                    margin: 0;
                                    padding: 0;
                                    background-color: #f4f4f4;
                                }
                                .container {
                                    max-width: 600px;
                                    margin: 0 auto;
                                    padding: 0;
                                    background: white;
                                }
                                .header {
                                    background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                                    color: white;
                                    padding: 30px;
                                    text-align: center;
                                }
                                .content {
                                    padding: 30px;
                                }
                                .token {
                                    background: white;
                                    padding: 20px;
                                    margin: 20px 0;
                                    border-radius: 5px;
                                    border-left: 4px solid #667eea;
                                    font-family: monospace;
                                    font-size: 24px;
                                    font-weight: bold;
                                    text-align: center;
                                    letter-spacing: 2px;
                                    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                }
                                .warning {
                                    background: #fff3cd;
                                    border: 1px solid #ffeaa7;
                                    padding: 15px;
                                    border-radius: 5px;
                                    margin: 20px 0;
                                }
                                .footer {
                                    margin-top: 30px;
                                    padding-top: 20px;
                                    border-top: 1px solid #ddd;
                                    color: #666;
                                    font-size: 12px;
                                    text-align: center;
                                }
                                h1 { margin: 0; }
                                h2 { color: #2c3e50; }
                                p { margin-bottom: 15px; }
                                ul { margin: 10px 0; padding-left: 20px; }
                                li { margin-bottom: 5px; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>Verification Required</h1>
                                </div>
                                <div class="content">
                                    <h2>%s</h2>
                                    <p>Please use the following verification code to complete your request:</p>

                                    <div class="token">%s</div>

                                    <div class="warning">
                                        <strong>Important:</strong>
                                        <ul>
                                            <li>This code will expire in %d minutes</li>
                                            <li>Do not share this code with anyone</li>
                                            <li>If you didn't request this, please ignore this email</li>
                                        </ul>
                                    </div>

                                    <p style="font-size: 14px;">If you have any issues, please contact <a href="mailto:support@easyerpshop.app">support@easyerpshop.app</a></p>
                                </div>
                                <div class="footer">
                                    <p>© %d EasyErpShop. All rights reserved.</p>
                                    <p>This is an automated message, please do not reply to this email.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                action,
                token,
                expiryMinutes,
                currentYear);
    }

    private String getActionDescription(VerificationType type) {
        return switch (type) {
            case USER_REGISTRATION -> "Complete Your Account Registration";
            case PASSWORD_RESET -> "Reset Your Password";
            case EMAIL_OTP_LOGIN -> "Login to Your Account";
            case EMAIL_CHANGE_CONFIRMATION -> "Confirm Your New Email Address";
            case COMPANY_VERIFICATION -> "Verify Your Company Account";
            case WAREHOUSE_VERIFICATION -> "Verify Warehouse Access";
            case TWO_FACTOR_AUTH -> "Two-Factor Authentication";
            case TRANSACTION_CONFIRMATION -> "Confirm Transaction";
            case DOCUMENT_APPROVAL -> "Approve Document";
            default -> "Complete Verification";
        };
    }

}