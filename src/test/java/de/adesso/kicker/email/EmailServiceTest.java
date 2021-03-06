package de.adesso.kicker.email;

import de.adesso.kicker.match.persistence.Match;
import de.adesso.kicker.notification.matchverificationrequest.persistence.MatchVerificationRequest;
import de.adesso.kicker.notification.matchverificationrequest.service.events.MatchVerificationSentEvent;
import de.adesso.kicker.user.persistence.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class EmailServiceTest {

    @Mock
    private JavaMailSenderImpl mailSenderMock;

    @Mock
    private MatchVerificationSentEvent matchVerificationSentEventMock;

    @Mock
    private Match matchMock;

    @Mock
    private MatchVerificationRequest matchVerificationRequestMock;

    @Mock
    private User teamAPlayer1Mock;
    @Mock
    private User teamAPlayer2Mock;

    @Mock
    private User teamBPlayer1Mock;
    @Mock
    private User teamBPlayer2Mock;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void when1v1thenSendEmail() {
        // given
        var matchId = "test-match-1";
        var notificationId = 1337L;
        var teamAPlayer1Mail = "teamAPlayer1@test.com";
        var teamBPlayer1Mail = "teamBPlayer1@test.com";
        ArrayList<String> teamAPlayer1FullName = new ArrayList<>();
        teamAPlayer1FullName.add("Full Name PlayerA1");

        ArrayList<String> teamBPlayer1FullName = new ArrayList<>();
        teamBPlayer1FullName.add("Full Name PlayerB1");

        var matchDate = LocalDate.now();
        var expectedAcceptUrl = EmailService.ACCEPT_URL + notificationId;
        var expectedDeclineUrl = EmailService.DECLINE_URL + notificationId;
        var expectedWinnerText = String.format("Winners: %s\tLosers:%s", teamAPlayer1FullName, teamBPlayer1FullName);
        var expectedMessageSubject = String.format("Verify Match: %s played on %s", matchId, matchDate.toString());
        var expectedMessageText = String.format(
                "Your recently played Match against %s needs to be verified.\n%s\nVerify -> %s\nDecline -> %s",
                teamAPlayer1FullName.get(0), expectedWinnerText, expectedAcceptUrl, expectedDeclineUrl);

        var expectedSimpleMailMessage = new SimpleMailMessage();
        expectedSimpleMailMessage.setFrom(teamAPlayer1Mail);
        expectedSimpleMailMessage.setTo(teamBPlayer1Mail);
        expectedSimpleMailMessage.setSubject(expectedMessageSubject);
        expectedSimpleMailMessage.setText(expectedMessageText);

        given(teamAPlayer1Mock.getEmail()).willReturn(teamAPlayer1Mail);
        given(teamAPlayer1Mock.getFullName()).willReturn(teamAPlayer1FullName.get(0));

        given(teamBPlayer1Mock.getEmail()).willReturn(teamBPlayer1Mail);
        given(teamBPlayer1Mock.getFullName()).willReturn(teamBPlayer1FullName.get(0));

        given(matchMock.getMatchId()).willReturn(matchId);
        given(matchMock.getDate()).willReturn(matchDate);
        given(matchMock.getWinners()).willReturn(List.of(teamAPlayer1Mock));
        given(matchMock.getLosers()).willReturn(List.of(teamBPlayer1Mock));

        given(matchVerificationSentEventMock.getMatchVerificationRequest()).willReturn(matchVerificationRequestMock);
        given(matchVerificationRequestMock.getMatch()).willReturn(matchMock);
        given(matchVerificationRequestMock.getReceiver()).willReturn(teamBPlayer1Mock);
        given(matchVerificationRequestMock.getNotificationId()).willReturn(notificationId);

        given(matchMock.getTeamAPlayer1()).willReturn(teamAPlayer1Mock);

        // when
        emailService.sendVerification(matchVerificationSentEventMock);

        // then
        verify(mailSenderMock).send(expectedSimpleMailMessage);
    }

    @Test
    void when1v2thenSendEmail() {
        // given
        var matchId = "test-match-1";
        var notificationId = 1337L;

        var teamAPlayer1Mail = "teamAPlayer1@test.com";

        var teamBPlayer1Mail = "teamBPlayer1@test.com";
        var teamBPlayer2Mail = "teamBPlayer2@test.com";

        ArrayList<String> teamAPlayer1FullName = new ArrayList<>();
        teamAPlayer1FullName.add("Full Name PlayerA1");

        ArrayList<String> teamBPlayerFullNames = new ArrayList<>();
        teamBPlayerFullNames.add("Full Name PlayerB1");
        teamBPlayerFullNames.add("Full Name PlayerB2");

        var matchDate = LocalDate.now();

        var expectedAcceptUrl = EmailService.ACCEPT_URL + notificationId;
        var expectedDeclineUrl = EmailService.DECLINE_URL + notificationId;
        var expectedWinnerText = String.format("Winners: %s\tLosers:%s", teamAPlayer1FullName, teamBPlayerFullNames);
        var expectedMessageSubject = String.format("Verify Match: %s played on %s", matchId, matchDate.toString());
        var expectedMessageText = String.format(
                "Your recently played Match against %s needs to be verified.\n%s\nVerify -> %s\nDecline -> %s",
                teamAPlayer1FullName.get(0), expectedWinnerText, expectedAcceptUrl, expectedDeclineUrl);

        var expectedSimpleMailMessage = new SimpleMailMessage();
        expectedSimpleMailMessage.setFrom(teamAPlayer1Mail);
        expectedSimpleMailMessage.setTo(teamBPlayer1Mail);
        expectedSimpleMailMessage.setSubject(expectedMessageSubject);
        expectedSimpleMailMessage.setText(expectedMessageText);

        given(teamAPlayer1Mock.getEmail()).willReturn(teamAPlayer1Mail);
        given(teamAPlayer1Mock.getFullName()).willReturn(teamAPlayer1FullName.get(0));

        given(teamBPlayer1Mock.getEmail()).willReturn(teamBPlayer1Mail);
        given(teamBPlayer1Mock.getFullName()).willReturn(teamBPlayerFullNames.get(0));
        given(teamBPlayer2Mock.getEmail()).willReturn(teamBPlayer2Mail);
        given(teamBPlayer2Mock.getFullName()).willReturn(teamBPlayerFullNames.get(1));

        given(matchMock.getMatchId()).willReturn(matchId);
        given(matchMock.getDate()).willReturn(matchDate);
        given(matchMock.getWinners()).willReturn(List.of(teamAPlayer1Mock));
        given(matchMock.getLosers()).willReturn(List.of(teamBPlayer1Mock, teamBPlayer2Mock));

        given(matchVerificationSentEventMock.getMatchVerificationRequest()).willReturn(matchVerificationRequestMock);
        given(matchVerificationRequestMock.getMatch()).willReturn(matchMock);
        given(matchVerificationRequestMock.getReceiver()).willReturn(teamBPlayer1Mock, teamAPlayer2Mock);
        given(matchVerificationRequestMock.getNotificationId()).willReturn(notificationId);

        given(matchMock.getTeamAPlayer1()).willReturn(teamAPlayer1Mock);

        // when
        emailService.sendVerification(matchVerificationSentEventMock);

        // then
        verify(mailSenderMock).send(expectedSimpleMailMessage);
    }

    @Test
    void when2v1thenSendEmail() {
        // given
        var matchId = "test-match-1";

        var notificationId = 1337L;

        var teamAPlayer1Mail = "teamAPlayer1@test.com";
        var teamAPlayer2Mail = "teamAPlayer2@test.com";
        var teamBPlayer1Mail = "teamBPlayer1@test.com";

        ArrayList<String> teamAPlayerFullNames = new ArrayList<>();
        teamAPlayerFullNames.add("Full Name PlayerA1");
        teamAPlayerFullNames.add("Full Name PlayerA2");

        ArrayList<String> teamBPlayerFullNames = new ArrayList<>();
        teamBPlayerFullNames.add("Full Name PlayerB1");

        var matchDate = LocalDate.now();
        var expectedAcceptUrl = EmailService.ACCEPT_URL + notificationId;
        var expectedDeclineUrl = EmailService.DECLINE_URL + notificationId;
        var expectedWinnerText = String.format("Winners: %s\tLosers:%s", teamAPlayerFullNames, teamBPlayerFullNames);
        var expectedMessageSubject = String.format("Verify Match: %s played on %s", matchId, matchDate.toString());
        var expectedMessageText = String.format(
                "Your recently played Match against %s and %s needs to be verified.\n%s\nVerify -> %s\nDecline -> %s",
                teamAPlayerFullNames.get(0), teamAPlayerFullNames.get(1), expectedWinnerText, expectedAcceptUrl,
                expectedDeclineUrl);

        var expectedSimpleMailMessage = new SimpleMailMessage();
        expectedSimpleMailMessage.setFrom(teamAPlayer1Mail);
        expectedSimpleMailMessage.setTo(teamBPlayer1Mail);
        expectedSimpleMailMessage.setSubject(expectedMessageSubject);
        expectedSimpleMailMessage.setText(expectedMessageText);

        given(teamAPlayer1Mock.getEmail()).willReturn(teamAPlayer1Mail);
        given(teamAPlayer1Mock.getFullName()).willReturn(teamAPlayerFullNames.get(0));

        given(teamAPlayer2Mock.getEmail()).willReturn(teamAPlayer2Mail);
        given(teamAPlayer2Mock.getFullName()).willReturn(teamAPlayerFullNames.get(1));

        given(teamBPlayer1Mock.getEmail()).willReturn(teamBPlayer1Mail);
        given(teamBPlayer1Mock.getFullName()).willReturn(teamBPlayerFullNames.get(0));

        given(matchMock.getMatchId()).willReturn(matchId);
        given(matchMock.getDate()).willReturn(matchDate);
        given(matchMock.getWinners()).willReturn(List.of(teamAPlayer1Mock, teamAPlayer2Mock));
        given(matchMock.getLosers()).willReturn(List.of(teamBPlayer1Mock));
        given(matchMock.getTeamAPlayer2()).willReturn(teamAPlayer2Mock);

        given(matchVerificationSentEventMock.getMatchVerificationRequest()).willReturn(matchVerificationRequestMock);
        given(matchVerificationRequestMock.getMatch()).willReturn(matchMock);
        given(matchVerificationRequestMock.getReceiver()).willReturn(teamBPlayer1Mock);
        given(matchVerificationRequestMock.getNotificationId()).willReturn(notificationId);
        given(matchMock.getTeamAPlayer1()).willReturn(teamAPlayer1Mock);

        // when
        emailService.sendVerification(matchVerificationSentEventMock);

        // then
        verify(mailSenderMock).send(expectedSimpleMailMessage);
    }

    @Test
    void when2v2thenSendEmail() {
        // given
        var matchId = "test-match-1";

        var notificationId = 1337L;

        var teamAPlayer1Mail = "teamAPlayer1@test.com";
        var teamAPlayer2Mail = "teamAPlayer2@test.com";
        var teamBPlayer1Mail = "teamBPlayer1@test.com";
        var teamBPlayer2Mail = "teamBPlayer2@test.com";

        ArrayList<String> teamAPlayerFullNames = new ArrayList<>();
        teamAPlayerFullNames.add("Full Name PlayerA1");
        teamAPlayerFullNames.add("Full Name PlayerA2");

        ArrayList<String> teamBPlayerFullNames = new ArrayList<>();
        teamBPlayerFullNames.add("Full Name PlayerB1");
        teamBPlayerFullNames.add("Full Name PlayerB2");

        var matchDate = LocalDate.now();
        var expectedAcceptUrl = EmailService.ACCEPT_URL + notificationId;
        var expectedDeclineUrl = EmailService.DECLINE_URL + notificationId;
        var expectedWinnerText = String.format("Winners: %s\tLosers:%s", teamAPlayerFullNames, teamBPlayerFullNames);
        var expectedMessageSubject = String.format("Verify Match: %s played on %s", matchId, matchDate.toString());
        var expectedMessageText = String.format(
                "Your recently played Match against %s and %s needs to be verified.\n%s\nVerify -> %s\nDecline -> %s",
                teamAPlayerFullNames.get(0), teamAPlayerFullNames.get(1), expectedWinnerText, expectedAcceptUrl,
                expectedDeclineUrl);

        var expectedSimpleMailMessage = new SimpleMailMessage();
        expectedSimpleMailMessage.setFrom(teamAPlayer1Mail);
        expectedSimpleMailMessage.setTo(teamBPlayer1Mail);
        expectedSimpleMailMessage.setSubject(expectedMessageSubject);
        expectedSimpleMailMessage.setText(expectedMessageText);

        given(teamAPlayer1Mock.getEmail()).willReturn(teamAPlayer1Mail);
        given(teamAPlayer1Mock.getFullName()).willReturn(teamAPlayerFullNames.get(0));

        given(teamAPlayer2Mock.getEmail()).willReturn(teamAPlayer2Mail);
        given(teamAPlayer2Mock.getFullName()).willReturn(teamAPlayerFullNames.get(1));

        given(teamBPlayer1Mock.getEmail()).willReturn(teamBPlayer1Mail);
        given(teamBPlayer1Mock.getFullName()).willReturn(teamBPlayerFullNames.get(0));

        given(teamBPlayer2Mock.getEmail()).willReturn(teamBPlayer2Mail);
        given(teamBPlayer2Mock.getFullName()).willReturn(teamBPlayerFullNames.get(1));

        given(matchMock.getMatchId()).willReturn(matchId);
        given(matchMock.getDate()).willReturn(matchDate);
        given(matchMock.getWinners()).willReturn(List.of(teamAPlayer1Mock, teamAPlayer2Mock));
        given(matchMock.getLosers()).willReturn(List.of(teamBPlayer1Mock, teamBPlayer2Mock));
        given(matchMock.getTeamAPlayer2()).willReturn(teamAPlayer2Mock);

        given(matchVerificationSentEventMock.getMatchVerificationRequest()).willReturn(matchVerificationRequestMock);
        given(matchVerificationRequestMock.getMatch()).willReturn(matchMock);
        given(matchVerificationRequestMock.getReceiver()).willReturn(teamBPlayer1Mock, teamBPlayer2Mock);
        given(matchVerificationRequestMock.getNotificationId()).willReturn(notificationId);

        given(matchMock.getTeamAPlayer1()).willReturn(teamAPlayer1Mock);

        // when
        emailService.sendVerification(matchVerificationSentEventMock);

        // then
        verify(mailSenderMock).send(expectedSimpleMailMessage);
    }

}
