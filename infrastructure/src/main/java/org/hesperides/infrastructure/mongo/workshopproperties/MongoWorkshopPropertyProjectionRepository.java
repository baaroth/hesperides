package org.hesperides.infrastructure.mongo.workshopproperties;

import java.util.Optional;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hesperides.domain.WorkshopPropertyByKeyQuery;
import org.hesperides.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.domain.WorkshopPropertyUpdatedEvent;
import org.hesperides.domain.workshopproperties.WorkshopPropertyProjectionRepository;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import static org.hesperides.domain.framework.Profiles.FAKE_MONGO;
import static org.hesperides.domain.framework.Profiles.MONGO;

@Profile({MONGO, FAKE_MONGO})
@Repository
public class MongoWorkshopPropertyProjectionRepository implements WorkshopPropertyProjectionRepository {

    private final MongoWorkshopPropertyRepository repository;

    @Autowired
    public MongoWorkshopPropertyProjectionRepository(MongoWorkshopPropertyRepository repository) {
        this.repository = repository;
    }

    /*** EVENT HANDLERS ***/

    @EventSourcingHandler
    @Override
    public void on(WorkshopPropertyCreatedEvent event) {
        final WorkshopPropertyDocument doc = WorkshopPropertyDocument.fromDomainInstance(event.getDefinition());
        repository.save(doc);
    }

    @EventSourcingHandler
    @Override
    public void on(WorkshopPropertyUpdatedEvent event) {
        final WorkshopPropertyDocument doc = WorkshopPropertyDocument.fromDomainInstance(event.getNewDefinition());
        repository.save(doc);
    }

    /*** QUERY HANDLERS ***/

    @QueryHandler
    @Override
    public Optional<WorkshopPropertyView> query(WorkshopPropertyByKeyQuery query) {
        return repository.findOptionalByKey(query.getKey())
                .map(WorkshopPropertyDocument::toWorkshopPropertyView);
    }
}
