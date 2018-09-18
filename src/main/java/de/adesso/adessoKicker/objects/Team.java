package de.adesso.adessoKicker.objects;


import javax.persistence.*;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long teamId;

    private String teamName;

    private long teamWins;
 
    private long teamLosses;

    @OneToOne(targetEntity = User.class)
    private User playerA;

    @OneToOne(targetEntity = User.class)
    private User playerB;
   
    /* 
    * Ein Team kann unabhängig von einem Tournament existieren
    @OneToOne(targetEntity = Tournament.class)
    private Tournament tournament;
	*
	*/
    protected Team() {
        User playerA = new User();
        User playerB = new User();
    }

    public Team(String teamName) {

        this.teamName = teamName;
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public long getTeamWins() {
        return teamWins;
    }

    public void setTeamWins(long teamWins) {
        this.teamWins = teamWins;
    }
/*
    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
*/
    public long getTeamLosses() {
        return teamLosses;
    }

    public void setTeamLosses(long teamLosses) {
        this.teamLosses = teamLosses;
    }

    public User getPlayerA() {
        return playerA;
    }

    public void setPlayerA(User playerA) {
        this.playerA = playerA;
    }

    public User getPlayerB() {
        return playerB;
    }

    public void setPlayerB(User playerB) {
        this.playerB = playerB;
    }

}