package co.turtlegames.core.recharge;

import java.util.UUID;

public class RechargeToken {

    private UUID _playerUuid;
    private long _start;
    private long _duration;

    public RechargeToken(UUID playerUuid, long duration) {
        _playerUuid = playerUuid;
        _start = System.currentTimeMillis();
        _duration = duration;
    }

    public boolean hasFinished() {
        return System.currentTimeMillis() >= getFinishTime();
    }

    public long getFinishTime() {
        return _start + _duration;
    }

    public UUID getPlayerUuid() {
        return _playerUuid;
    }

    public long getStart() {
        return _start;
    }

    public long getDuration() {
        return _duration;
    }

}
