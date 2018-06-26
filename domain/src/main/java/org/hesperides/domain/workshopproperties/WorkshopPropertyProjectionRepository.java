package org.hesperides.domain.workshopproperties;

import java.util.Optional;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hesperides.domain.WorkshopPropertyByKeyQuery;
import org.hesperides.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;

public interface WorkshopPropertyProjectionRepository {

    /*** EVENT HANDLERS ***/

    @EventSourcingHandler
    void on(WorkshopPropertyCreatedEvent event);

    /*** QUERY HANDLERS ***/

    @QueryHandler
    Optional<WorkshopPropertyView> query(WorkshopPropertyByKeyQuery query);
}
