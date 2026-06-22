package com.finshop.finshop.service;

import com.finshop.finshop.model.entity.Order;
import com.finshop.finshop.model.entity.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(order.getUser().getEmail());
            helper.setFrom("noreply@finshop.com");
            helper.setSubject("Confirmation de votre commande #" + order.getId());
            helper.setText(buildEmailContent(order), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

    private String buildEmailContent(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Merci pour votre commande !</h2>");
        sb.append("<p>Bonjour <strong>").append(order.getUser().getPrenom()).append("</strong>,</p>");
        sb.append("<p>Votre commande <strong>#").append(order.getId()).append("</strong> a été confirmée.</p>");
        sb.append("<h3>Détails de la commande :</h3>");
        sb.append("<table border='1' cellpadding='8'>");
        sb.append("<tr><th>Produit</th><th>Quantité</th><th>Prix unitaire</th><th>Sous-total</th></tr>");

        for (OrderItem item : order.getItems()) {
            sb.append("<tr>");
            sb.append("<td>").append(item.getProduct().getNom()).append("</td>");
            sb.append("<td>").append(item.getQuantite()).append("</td>");
            sb.append("<td>").append(item.getPrixUnitaire()).append(" €</td>");
            sb.append("<td>").append(item.getPrixUnitaire()
                            .multiply(java.math.BigDecimal.valueOf(item.getQuantite())))
                    .append(" €</td>");
            sb.append("</tr>");
        }

        sb.append("</table>");
        sb.append("<p><strong>Total : ").append(order.getTotal()).append(" €</strong></p>");
        sb.append("<p>Merci de votre confiance !</p>");
        sb.append("<p>L'équipe FinShop</p>");

        return sb.toString();
    }
}
