package co.turtlegames.core.infraction;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import java.util.*;

public class InfractionData {

    private InfractionManager _managerInstance;
    private UUID _ownerUuid;

    private Multimap<InfractionType, Infraction> _infractions;

    public InfractionData(InfractionManager manager, UUID owner, Collection<Infraction> infractions) {

        _managerInstance = manager;
        _ownerUuid = owner;

        _infractions = MultimapBuilder.enumKeys(InfractionType.class)
                                .arrayListValues()
                                    .build();

        for(Infraction infraction : infractions) {
            _infractions.put(infraction.getType(), infraction);
        }

    }

    protected void applyRemoval(long epoch, UUID removedBy, String reason) {

        for (Infraction infraction : getAllInfractions()) {

            if (infraction.getIssueEpoch() == epoch) {

                infraction.setRemoved(true);
                infraction.setRemovedBy(removedBy);
                infraction.setRemoveReason(reason);

            }

        }

    }

    public Collection<Infraction> getAllInfractions() {
        return _infractions.values();
    }

    public Collection<Infraction> getInfractionOfType(InfractionType type) {
        return _infractions.get(type);
    }

    public Infraction getRelevantInfraction(InfractionType type) {

        return _infractions.get(type)
                .stream()
                .filter((Infraction inf) -> inf.getType() == type && inf.isActive())
                    .findFirst()
                        .orElse(null);

    }

    public void registerInfraction(Infraction infraction) {
        _infractions.put(infraction.getType(), infraction);
    }
}
