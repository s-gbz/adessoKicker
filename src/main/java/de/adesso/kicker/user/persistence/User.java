package de.adesso.kicker.user.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {

    @Id
    private String userId;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;

    private boolean sendEmail;

    private long wins;
    private long losses;

    @OneToOne(cascade = CascadeType.ALL)
    private Statistic statistic;

    public User(String userId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sendEmail = true;
        this.wins = 0;
        this.losses = 0;
    }

    public int getWinRatio() {
        if (getPlayedMatches() == 0 || losses == 0) {
            return 100;
        }
        return Math.round(100 - (100f / getPlayedMatches() * losses));
    }

    public Long getPlayedMatches() {
        return wins + losses;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
