package de.adesso.kicker.notification.matchverificationrequest;

import de.adesso.kicker.match.MatchDummy;
import de.adesso.kicker.notification.matchverificationrequest.persistence.MatchVerificationRequest;
import de.adesso.kicker.user.UserDummy;

public class MatchVerificationRequestDummy {

    public static MatchVerificationRequest matchVerificationRequest() {
        return new MatchVerificationRequest(UserDummy.defaultUser(), UserDummy.alternateUser(), MatchDummy.match());
    }
}
