package org.hesperides.tests.bdd.modules.scenarios;

import cucumber.api.java8.En;
import org.hesperides.domain.templatecontainer.entities.TemplateContainer;
import org.hesperides.presentation.io.ModuleIO;
import org.hesperides.tests.bdd.CucumberSpringBean;
import org.hesperides.tests.bdd.modules.ModuleAssertions;
import org.hesperides.tests.bdd.modules.ModuleSamples;
import org.hesperides.tests.bdd.modules.contexts.ModuleContext;
import org.hesperides.tests.bdd.technos.contexts.TechnoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class CopyAModule extends CucumberSpringBean implements En {

    @Autowired
    private ModuleContext moduleContext;
    @Autowired
    private TechnoContext technoContext;

    private ResponseEntity<ModuleIO> response;

    public CopyAModule() {

        Given("^the techno is attached to the module$", () -> {
            ModuleIO moduleInput = ModuleSamples.getModuleInputWithTechnoAndVersionId(technoContext.getTechnoKey(), 1);
            moduleContext.updateModule(moduleInput);
        });

        When("^creating a copy of this module$", () -> {
            ModuleIO moduleInput = ModuleSamples.getModuleInputWithNameAndVersion("module-copy", "1.0.1");
            response = copyModule(moduleInput);
        });

        Then("^the module is successfully and completely duplicated$", () -> {
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            ModuleIO actualModuleOutput = response.getBody();
            ModuleIO expectedModuleOutput = ModuleSamples.getModuleInputWithNameAndVersionAndTechno("module-copy", "1.0.1", technoContext.getTechnoKey());
            ModuleAssertions.assertModule(expectedModuleOutput, actualModuleOutput, 1L);
        });
    }

    public ResponseEntity<ModuleIO> copyModule(ModuleIO moduleInput) {
        TemplateContainer.Key moduleKey = moduleContext.getModuleKey();
        return rest.getTestRest().postForEntity("/modules?from_module_name={moduleName}&from_module_version={moduleVersion}&from_is_working_copy={isWorkingCopy}",
                moduleInput, ModuleIO.class,
                moduleKey.getName(), moduleKey.getVersion(), moduleKey.isWorkingCopy());
    }

    //TODO Assert templates
}
