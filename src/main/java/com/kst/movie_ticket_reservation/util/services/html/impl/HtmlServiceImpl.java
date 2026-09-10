package com.kst.movie_ticket_reservation.util.services.html.impl;

import com.kst.movie_ticket_reservation.util.services.html.HtmlService;
import org.springframework.stereotype.Service;

@Service
public class HtmlServiceImpl implements HtmlService
{
    @Override
    public String generateRequestOtpMailSendTemplate(String otp)
    {
        return """
                <!DOCTYPE html>
                    <html lang="en">
                    <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <meta name="color-scheme" content="light">
                    <meta name="supported-color-schemes" content="light">
                    <title>Your Verification Code</title>
                    </head>
                    <body style="margin: 0; padding: 0; background-color: #f8fafc; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; -webkit-font-smoothing: antialiased;">
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tr>
                    <td align="center" style="padding: 48px 20px;">
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 480px; background-color: #ffffff; border-radius: 16px; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05); border: 1px solid #e2e8f0;">
                
                    <!-- Header -->
                    <tr>
                    <td align="center" style="padding: 48px 40px 32px;">
                    <div style="width: 48px; height: 48px; background-color: #0f172a; border-radius: 12px; display: inline-block; line-height: 48px; text-align: center; margin-bottom: 24px;">
                    <span style="font-size: 24px;">🎬</span>
                    </div>
                    <h1 style="margin: 0; font-size: 20px; font-weight: 600; color: #0f172a; letter-spacing: -0.5px;">KST Cinemas</h1>
                    </td>
                    </tr>
                
                    <!-- Divider -->
                    <tr>
                    <td style="padding: 0 40px;">
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tr><td style="border-top: 1px solid #f1f5f9;"></td></tr>
                    </table>
                    </td>
                    </tr>
                
                    <!-- Content -->
                    <tr>
                    <td style="padding: 32px 40px 24px; text-align: center;">
                    <h2 style="margin: 0 0 12px; font-size: 24px; font-weight: 600; color: #0f172a; letter-spacing: -0.5px;">Verification Code</h2>
                    <p style="margin: 0; font-size: 15px; line-height: 1.6; color: #64748b;">
                    Use the following code to verify your identity and access your account.
                    </p>
                    </td>
                    </tr>
                
                    <!-- OTP Code -->
                    <tr>
                    <td align="center" style="padding: 8px 40px 32px;">
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                    <td style="background-color: #f8fafc; border-radius: 12px; padding: 24px 40px; border: 1px solid #e2e8f0;">
                    <span style="font-family: 'SF Mono', Monaco, 'Cascadia Code', monospace; font-size: 32px; font-weight: 700; color: #0f172a; letter-spacing: 8px;">
                """ + otp + """
                </span>
                </td>
                </tr>
                </table>
                </td>
                </tr>
                
                <!-- Expiry -->
                <tr>
                <td align="center" style="padding: 0 40px 40px;">
                <p style="margin: 0; font-size: 13px; color: #94a3b8; font-weight: 500;">
                                                    ⏱ This code expires in 5 minutes
                </p>
                </td>
                </tr>
                
                <!-- Divider -->
                <tr>
                <td style="padding: 0 40px;">
                <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr><td style="border-top: 1px solid #f1f5f9;"></td></tr>
                </table>
                </td>
                </tr>
                
                <!-- Footer -->
                <tr>
                <td style="padding: 24px 40px 48px; text-align: center;">
                <p style="margin: 0 0 8px; font-size: 13px; color: #94a3b8; line-height: 1.5;">
                Didn't request this? You can safely ignore this email.
                </p>
                <p style="margin: 0; font-size: 12px; color: #cbd5e1;">
                &copy; 2026 KST Movie Ticket Reservation
                                                </p>
                </td>
                </tr>
                
                </table>
                </td>
                </tr>
                </table>
                </body>
                </html>
                """;
    }
}
