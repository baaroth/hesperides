/*
 *
 * This file is part of the Hesperides distribution.
 * (https://github.com/voyages-sncf-technologies/hesperides)
 * Copyright (c) 2016 VSCT.
 *
 * Hesperides is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, version 3.
 *
 * Hesperides is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package org.hesperides.domain.workshopproperties.commands;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.hesperides.domain.CreateWorkshopPropertyCommand;
import org.hesperides.domain.UpdateWorkshopPropertyCommand;
import org.hesperides.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.domain.WorkshopPropertyUpdatedEvent;
import org.hesperides.domain.workshopproperties.entities.WorkshopProperty;

import java.io.Serializable;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Slf4j
@Aggregate
@NoArgsConstructor
public class WorkshopPropertyAggregate implements Serializable {

    @AggregateIdentifier
    private String key;

    private int lastVersion;

    @CommandHandler
    public WorkshopPropertyAggregate(CreateWorkshopPropertyCommand cmd) {
        final WorkshopProperty initial = cmd.getDefinition();
        final String k = initial.getKey(), v = initial.getValue();
        final WorkshopProperty prop = new WorkshopProperty(k, v, String.format("%s:%s", k, v), 1);

        AggregateLifecycle.apply(new WorkshopPropertyCreatedEvent(prop, cmd.getUser()));
    }

    @CommandHandler
    public void dispatch(UpdateWorkshopPropertyCommand cmd) {
        log.debug("Applying update workshop-property command...");
        final WorkshopProperty initial = cmd.getNewDefinition();
        final String k = initial.getKey(), v = initial.getValue();
        final int version = initial.getVersion() + 1;
        final WorkshopProperty prop = new WorkshopProperty(k, v, String.format("%s:%s", k, v), version);

        apply(new WorkshopPropertyUpdatedEvent(k, prop, cmd.getUser()));
    }

    @EventSourcingHandler
    public void on(WorkshopPropertyCreatedEvent event) {
        this.key = event.getDefinition().getKey();
        this.lastVersion = 1;
        log.debug("workshop property created");
    }

    @EventSourcingHandler
    public void on(WorkshopPropertyUpdatedEvent event) {
        lastVersion = event.getNewDefinition().getVersion();
        log.debug("workshop property updated");
    }
}
