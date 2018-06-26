package org.hesperides.application.workshopproperties;

import org.hesperides.domain.security.User;
import org.hesperides.domain.workshopproperties.commands.WorkshopPropertyCommands;
import org.hesperides.domain.workshopproperties.entities.WorkshopProperty;
import org.hesperides.domain.workshopproperties.exceptions.DuplicateWorkshopPropertyException;
import org.hesperides.domain.workshopproperties.exceptions.WorkshopPropertyNotFoundException;
import org.hesperides.domain.workshopproperties.queries.WorkshopPropertyQueries;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkshopPropertyUseCases {

    private final WorkshopPropertyCommands commands;
    private final WorkshopPropertyQueries queries;

    @Autowired
    public WorkshopPropertyUseCases(WorkshopPropertyCommands commands, WorkshopPropertyQueries queries) {
        this.commands = commands;
        this.queries = queries;
    }

    public String createWorkshopProperty(WorkshopProperty input, User user) {
        if (queries.workshopPropertyExists(input.getKey())) {
            throw new DuplicateWorkshopPropertyException(input.getKey());
        }
        return commands.createProperty(input, user);
    }

    public WorkshopPropertyView getWorkshopProperty(String k) {
        return queries.fetchProperty(k)
                .orElseThrow(() -> new WorkshopPropertyNotFoundException(k));
    }

    public void updateWorkshopProperty(WorkshopProperty input, User user) {
        commands.updateWorkshopProperty(input, user);
    }
}
