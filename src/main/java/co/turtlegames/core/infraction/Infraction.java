package co.turtlegames.core.infraction;

import java.util.UUID;

public class Infraction {

    private UUID _owner;
    private UUID _issuer;

    private InfractionType _type;

    private long _issueEpoch;
    private long _lengthMs;

    private String _reason;

    public Infraction(UUID holder, UUID issuer, InfractionType type, long issueEpoch, long lengthMs, String reason) {

        _owner = holder;
        _issuer = issuer;

        _type = type;

        _issueEpoch = issueEpoch;
        _lengthMs = lengthMs;

        _reason = reason;

    }

    public long getExpiry() {
        return _issueEpoch + _lengthMs;
    }

    public long getMsUntilExpiry() {
        return this.getExpiry() - System.currentTimeMillis();
    }

    public UUID getOwner() {
        return _owner;
    }

    public UUID getIssuer() {
        return _issuer;
    }

    public InfractionType getType() {
        return _type;
    }

    public long getIssueEpoch() {
        return _issueEpoch;
    }

    public long getDuration() {
        return _lengthMs;
    }

    public String getReason() {
        return _reason;
    }
}
