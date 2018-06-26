package org.hesperides.tests.bdd.workshopproperties.scenarios;

import cucumber.api.java8.En;
import org.hesperides.presentation.io.WorkshopPropertyOutput;
import org.hesperides.presentation.io.WorkshopPropertyUpdateInput;
import org.hesperides.tests.bdd.CucumberSpringBean;
import org.hesperides.tests.bdd.workshopproperties.WorkshopPropertyAssertions;
import org.hesperides.tests.bdd.workshopproperties.contexts.WorkshopPropertyContext;
import org.hesperides.tests.bdd.workshopproperties.samples.WorkshopPropertySamples;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class UpdateAWorkshopProperty extends CucumberSpringBean implements En {

    @Autowired
    private WorkshopPropertyContext workshopPropertyContext;

    private ResponseEntity<WorkshopPropertyOutput> response;

    public UpdateAWorkshopProperty() {

        When("^updating this workshop property$", () -> {
            WorkshopPropertyUpdateInput input = WorkshopPropertySamples.buildWorkshopPropertyUpdateInput("foo", 1);
            response = workshopPropertyContext.updateWorkshopProperty(input);
        });

        Then("^the workshop property is successfully updated", () -> {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            WorkshopPropertyOutput actualOutput = response.getBody();
            WorkshopPropertyOutput expectedOutput = WorkshopPropertySamples.getWorkshopPropertyOutputWithValue("foo", 2);
            WorkshopPropertyAssertions.assertWorkshopProperty(expectedOutput, actualOutput);
        });
    }
}
