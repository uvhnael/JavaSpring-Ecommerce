package org.uvhnael.ecomapi.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Model.Mail;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String from = "Vu Lee Shop <anhvuproktka123@gmail.com>";

    public void send(Mail mail) throws MessagingException {
        // Tạo đối tượng MimeMessage
        MimeMessage message = mailSender.createMimeMessage();

        // Sử dụng MimeMessageHelper để thiết lập các thuộc tính của email
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Thêm mã hóa UTF-8

        // Thiết lập thông tin email
        helper.setFrom(from); // Địa chỉ email gửi
        helper.setTo(mail.getTo());     // Địa chỉ email nhận
        helper.setSubject(mail.getSubject()); // Tiêu đề email
        helper.setText(mail.getContent(), true); // Nội dung email (HTML)

        // Gửi email
        mailSender.send(message);
    }

    public String orderSuccess() {
        String emailContent = "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #333;'>Thank you for your order from Vu Lee Shop!</h2>"
                + "<p style='font-size: 16px;'>We appreciate your business. Here are the details of your order:</p>"
                + "<table style='width: 100%; border-collapse: collapse;'>"
                + "<thead><tr>"
                + "<th style='text-align: left; padding: 8px; border-bottom: 2px solid #ddd;'>Product</th>"
                + "<th style='text-align: left; padding: 8px; border-bottom: 2px solid #ddd;'>Image</th>"
                + "<th style='text-align: right; padding: 8px; border-bottom: 2px solid #ddd;'>Price</th>"
                + "<th style='text-align: right; padding: 8px; border-bottom: 2px solid #ddd;'>Quantity</th>"
                + "<th style='text-align: right; padding: 8px; border-bottom: 2px solid #ddd;'>Total</th>"
                + "</tr></thead>"
                + "<tbody>"
                + "<tr>"
                + "<td style='padding: 8px; border-bottom: 1px solid #ddd;'>Product Name 1</td>"
                + "<td style='padding: 8px; border-bottom: 1px solid #ddd;'><img src='https://via.placeholder.com/100' alt='Product Image' style='width: 50px;'></td>"
                + "<td style='text-align: right; padding: 8px; border-bottom: 1px solid #ddd;'>$10.00</td>"
                + "<td style='text-align: right; padding: 8px; border-bottom: 1px solid #ddd;'>2</td>"
                + "<td style='text-align: right; padding: 8px; border-bottom: 1px solid #ddd;'>$20.00</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 8px; border-bottom: 1px solid #ddd;'>Product Name 2</td>"
                + "<td style='padding: 8px; border-bottom: 1px solid #ddd;'><img src='https://via.placeholder.com/100' alt='Product Image' style='width: 50px;'></td>"
                + "<td style='text-align: right; padding: 8px; border-bottom: 1px solid #ddd;'>$15.00</td>"
                + "<td style='text-align: right; padding: 8px; border-bottom: 1px solid #ddd;'>1</td>"
                + "<td style='text-align: right; padding: 8px; border-bottom: 1px solid #ddd;'>$15.00</td>"
                + "</tr>"
                + "</tbody>"
                + "<tfoot>"
                + "<tr>"
                + "<td colspan='4' style='text-align: right; padding: 8px; font-weight: bold;'>Total Price:</td>"
                + "<td style='text-align: right; padding: 8px; font-weight: bold;'>$35.00</td>"
                + "</tr>"
                + "</tfoot>"
                + "</table>"
                + "<p style='font-size: 14px;'>If you have any questions about your order, feel free to contact us at support@vuleeshop.com.</p>"
                + "<p style='font-size: 14px;'>Thanks again for choosing Vu Lee Shop!</p>"
                + "</body></html>";
        return emailContent;
    }
}
