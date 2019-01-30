package de.adesso.kicker.notification.tournamentjoinrequest;

import de.adesso.kicker.notification.Notification;
import de.adesso.kicker.notification.NotificationType;
import de.adesso.kicker.team.Team;
import de.adesso.kicker.tournament.Tournament;
import de.adesso.kicker.tournament.singleelimination.SingleElimination;
import de.adesso.kicker.user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class TournamentJoinRequest extends Notification {

    @ManyToOne(targetEntity = SingleElimination.class)
    private Tournament targetTournament;

    @OneToOne(targetEntity = Team.class)
    private Team targetTeam;

    public TournamentJoinRequest(User sender, User receiver, Team team, Tournament tournament) {
        super(sender, receiver);
        this.targetTournament = tournament;
        this.targetTeam = team;
        setMessage(generateMessage());
        setType(NotificationType.TournamentJoinRequest);
    }

    public String generateMessage() {
        return getSender().getFirstName() + " " + getSender().getLastName() + " asked you to join tournament: "
                + targetTournament.getTournamentName() + " with your team: " + targetTeam.getTeamName();
    }

    public Team getTargetTeam() {
        return targetTeam;
    }

    public Tournament getTargetTournament() {
        return targetTournament;
    }

    @Override
    public String toString() {
        return "TournamentJoinRequest{" + "targetTournament=" + targetTournament + ", targetTeam=" + targetTeam + "} "
                + super.toString();
    }
}
