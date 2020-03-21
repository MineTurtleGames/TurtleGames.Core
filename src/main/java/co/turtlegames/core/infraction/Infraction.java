package co.turtlegames.core.infraction;

import java.util.UUID;

public class Infraction {

    public static final UUID SYSTEM_BAN = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private int _id;

    private UUID _owner;
    private UUID _issuer;

    private InfractionType _type;

    private long _issueEpoch;
    private long _lengthMs;

    private String _reason;

    private boolean _removed;
    private UUID _removedBy;
    private String _removeReason;

    public Infraction(UUID owner, UUID issuer, InfractionType type, long issueEpoch, long lengthMs, String reason, boolean removed, UUID removedBy, String removeReason) {

        _owner = owner;
        _issuer = issuer;

        _type = type;

        _issueEpoch = issueEpoch;
        _lengthMs = lengthMs;

        _reason = reason;

        _removed = removed;
        _removedBy = removedBy;
        _removeReason = removeReason;

    }

    public Infraction(UUID holder, UUID issuer, InfractionType type, long issueEpoch, long lengthMs, String reason) {

        this(holder, issuer, type, issueEpoch, lengthMs, reason, false, null, null);

    }

    public int getId() {
        return _id;
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

    public boolean isExpired() {

        if  (_lengthMs < 0)
            return false;

        return System.currentTimeMillis() > _issueEpoch + _lengthMs;

    }

    public boolean isActive() {
        return !isExpired() && !isRemoved();
    }

    public boolean isRemoved() {
        return _removed;
    }

    public UUID getRemovedBy() {
        return _removedBy;
    }

    public String getRemoveReason() {
        return _removeReason;
    }

    public void setRemoved(boolean removed) {
        _removed = removed;
    }

    public void setRemovedBy(UUID removedBy) {
        _removedBy = removedBy;
    }

    public void setRemoveReason(String removeReason) {
        _removeReason = removeReason;
    }

    public void setId(int id) {
        _id = id;
    }
}
