package org.hesperides.presentation.controllers;

import org.hesperides.application.workshopproperties.WorkshopPropertyUseCases;
import org.hesperides.domain.security.User;
import org.hesperides.domain.workshopproperties.entities.WorkshopProperty;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;
import org.hesperides.presentation.io.WorkshopPropertyInput;
import org.hesperides.presentation.io.WorkshopPropertyOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/workshop/properties")
@RestController
public class WorkshopPropertiesController extends AbstractController {

    private final WorkshopPropertyUseCases cases;

    @Autowired
    public WorkshopPropertiesController(WorkshopPropertyUseCases cases) {
        this.cases = cases;
    }

    @PostMapping
    public ResponseEntity<WorkshopPropertyOutput> createWorkshopProperty(Authentication authentication,
                                                                         @Valid @RequestBody final WorkshopPropertyInput input) {
        User currentUser = User.fromAuthentication(authentication);
        WorkshopProperty prop = input.toDomainInstance();
        String key = cases.createWorkshopProperty(prop, currentUser);

        return getWorkshopProperty(key);
    }

    @GetMapping("/{key}")
    public ResponseEntity<WorkshopPropertyOutput> getWorkshopProperty(@PathVariable("key") final String k) {
        WorkshopPropertyView view = cases.getWorkshopProperty(k);
        WorkshopPropertyOutput output = WorkshopPropertyOutput.fromWorkshopPropertyView(view);
        return ResponseEntity.ok(output);
    }

    @PutMapping
    public ResponseEntity<WorkshopPropertyOutput> updateWorkshopProperty(Authentication authentication,
                                                                         @Valid @RequestBody final WorkshopPropertyInput workshopPropertyInput) {

        throw new UnsupportedOperationException("Not implemented");
    }
}
