package de.adesso.kicker.email;

import de.adesso.kicker.events.match.MatchVerificationSentEvent;
import de.adesso.kicker.match.persistence.Match;
import de.adesso.kicker.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final EmailMessageBuilder emailMessageBuilder;

    static final String ACCEPT_URL = "http://localhost/notifications/accept/";

    static final String DECLINE_URL = "http://localhost/notifications/decline/";

    @EventListener
    public void sendVerification(MatchVerificationSentEvent matchVerificationSentEvent) {
        var matchVerificationRequest = matchVerificationSentEvent.getMatchVerificationRequest();
        var match = matchVerificationRequest.getMatch();
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(match.getTeamAPlayer1().getEmail());
            messageHelper.setTo(matchVerificationRequest.getReceiver().getEmail());
            messageHelper.setSubject(setSubject(match));
            messageHelper.setText(verificationText(), true);
        };
        mailSender.send(messagePreparator);
    }

    private String setSubject(Match match) {
        return String.format("Verify Match: %s played on %s", match.getMatchId(), match.getDate().toString());
    }

//    private String verificationText(MatchVerificationSentEvent matchVerificationSentEvent) {
//        String acceptUrl = ACCEPT_URL + matchVerificationSentEvent.getMatchVerificationRequest().getNotificationId();
//        String declineUrl = DECLINE_URL + matchVerificationSentEvent.getMatchVerificationRequest().getNotificationId();
//
//        Match match = matchVerificationSentEvent.getMatchVerificationRequest().getMatch();
//
//        String playerA1Name = match.getTeamAPlayer1().getFullName();
//
//        User playerA2 = match.getTeamAPlayer2();
//
//        String winnerText = getWinner(match);
//
//        if (checkPlayerExist(playerA2)) {
//            String playerA2Name = match.getTeamAPlayer2().getFullName();
//            return String.format(
//                    "Your recently played Match against %s and %s needs to be verified.\n%s\nVerify -> %s\nDecline -> %s",
//                    playerA1Name, playerA2Name, winnerText, acceptUrl, declineUrl);
//        } else {
//            return String.format(
//                    "Your recently played Match against %s needs to be verified.\n%s\nVerify -> %s\nDecline -> %s",
//                    playerA1Name, winnerText, acceptUrl, declineUrl);
//        }
//    }

    private String verificationText() {
        var map = new HashMap<String, Object>();
        map.put("message", "Hi");
        return emailMessageBuilder.build(map, "email/verification.html");
    }

    private boolean checkPlayerExist(User user) {
        return Objects.nonNull(user);
    }

    private String getWinner(Match match) {
        ArrayList<String> winners = new ArrayList<>();
        for (User winner : match.getWinners()) {
            winners.add(winner.getFullName());
        }
        ArrayList<String> losers = new ArrayList<>();
        for (User loser : match.getLosers()) {
            losers.add(loser.getFullName());
        }
        return String.format("Winners: %s\tLosers:%s", winners, losers);
    }
}
