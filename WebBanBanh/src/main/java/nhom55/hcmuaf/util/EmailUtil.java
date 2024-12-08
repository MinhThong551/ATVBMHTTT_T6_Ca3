    package nhom55.hcmuaf.util;
    import javax.mail.*;
    import javax.mail.internet.*;
    import java.io.File;
    import java.io.FileWriter;
    import java.util.Properties;

    public class EmailUtil {

        public static void sendEmailWithAttachment(String recipient, String subject, String messageBody, String privateKey) {
            // Cấu hình server email
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Tài khoản Gmail (sử dụng App Password, không dùng mật khẩu thực)
            String username = "vuluudragonslayer@gmail.com";
            String password = "cchu dykb fdrk vpmm";

            // Tạo session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                // Tạo email
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(subject);

                // Nội dung email
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(messageBody);

                // Tạo file đính kèm private.txt
                File tempFile = File.createTempFile("private", ".txt");
                try (FileWriter writer = new FileWriter(tempFile)) {
                    writer.write(privateKey);
                }

                MimeBodyPart filePart = new MimeBodyPart();
                filePart.attachFile(tempFile);

                // Kết hợp nội dung email và file đính kèm
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(filePart);

                message.setContent(multipart);

                // Gửi email
                Transport.send(message);

                System.out.println("Email sent successfully with attachment!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
