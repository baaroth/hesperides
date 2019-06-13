package org.hesperides.test.bdd.modules.scenarios;

import cucumber.api.java8.En;
import org.apache.commons.lang3.StringUtils;
import org.hesperides.core.presentation.io.ModuleIO;
import org.hesperides.test.bdd.commons.CommonSteps;
import org.hesperides.test.bdd.commons.HesperidesScenario;
import org.hesperides.test.bdd.modules.ModuleBuilder;
import org.hesperides.test.bdd.modules.ModuleClient;
import org.hesperides.test.bdd.modules.ModuleHistory;
import org.hesperides.test.bdd.technos.TechnoBuilder;
import org.hesperides.test.bdd.templatecontainers.builders.ModelBuilder;
import org.hesperides.test.bdd.templatecontainers.builders.PropertyBuilder;
import org.hesperides.test.bdd.templatecontainers.builders.TemplateBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CreateModules extends HesperidesScenario implements En {

    @Autowired
    private ModuleClient moduleClient;
    @Autowired
    private ModuleBuilder moduleBuilder;
    @Autowired
    private ModuleHistory moduleHistory;
    @Autowired
    private TechnoBuilder technoBuilder;
    @Autowired
    private TemplateBuilder templateBuilder;
    @Autowired
    private PropertyBuilder propertyBuilder;
    @Autowired
    private ModelBuilder modelBuilder;
    @Autowired
    private CommonSteps commonSteps;

    public CreateModules() {

        Given("^an existing module" +
                "(?: named \"([^\"]*)\")?" +
                "( (?:and|with) (?:a|this) template(?: with a \"/\" in the title)?)?" +
                "( (?:and|with) properties)?" +
                "( (?:and|with) password properties)?" +
                "( (?:and|with) global properties)?" +
                "( (?:and|with) this techno)?$", (
                String moduleName,
                String withTemplate,
                String withProperties,
                String withPasswordProperties,
                String withGlobalProperties,
                String withThisTechno) -> {

            if (StringUtils.isNotEmpty(moduleName)) {
                moduleBuilder.reset();
                moduleBuilder.withName(moduleName);
            }

            if (StringUtils.isEmpty(withTemplate) || !withTemplate.contains("this")) {
                templateBuilder.reset();
            }
            if (StringUtils.isNotEmpty(withTemplate) && withTemplate.contains("\"/\" in the title")) {
                templateBuilder.withName("a/template");
            }
            if (StringUtils.isNotEmpty(withThisTechno)) {
                moduleBuilder.withTechno(technoBuilder.build());
            }

            commonSteps.ensureUserAuthIsSet();
            testContext.responseEntity = moduleClient.create(moduleBuilder.build());
            assertCreated();
            moduleBuilder.withVersionId(1);

            if (StringUtils.isNotEmpty(withProperties)) {
                addPropertyToBuilders("module-foo");
                addPropertyToBuilders("module-bar");
            }
            if (StringUtils.isNotEmpty(withPasswordProperties)) {
                addPropertyToBuilders("module-fuzz", true);
            }

            if (StringUtils.isNotEmpty(withGlobalProperties)) {
                addPropertyToBuilders("global-module-foo");
                addPropertyToBuilders("global-module-bar");
            }
            if (StringUtils.isNotEmpty(withTemplate) || StringUtils.isNotEmpty(withProperties) || StringUtils.isNotEmpty(withGlobalProperties)) {
                moduleBuilder.withTemplate(templateBuilder.build());
                moduleClient.addTemplate(templateBuilder.build(), moduleBuilder.build());
            }
            moduleHistory.addModule();
        });

        Given("^a module with (\\d+) versions$", (Integer nbVersions) -> {
            moduleBuilder.withName("new-module");
            for (int i = 0; i < nbVersions; i++) {
                moduleBuilder.withVersion("1." + i);
                testContext.responseEntity = moduleClient.create(moduleBuilder.build());
                assertCreated();
            }
        });

        Given("^a list of( \\d+)? modules( with different names)?(?: with the same name)?$", (String modulesCount, String withDifferentNames) -> {
            Integer modulesToCreateCount = StringUtils.isEmpty(modulesCount) ? 12 : Integer.valueOf(modulesCount.substring(1));
            for (int i = 0; i < modulesToCreateCount; i++) {
                if (StringUtils.isNotEmpty(withDifferentNames)) {
                    moduleBuilder.withName("new-module-" + i);
                } else {
                    moduleBuilder.withName("new-module");
                }
                moduleBuilder.withVersion("0.0." + (i + 1));
                testContext.responseEntity = moduleClient.create(moduleBuilder.build());
                assertCreated();
            }
        });

        Given("^a module to create" +
                "(?: with the same name and version)?" +
                "( with this techno)?" +
                "( but different letter case)?" +
                "( without a version type)?$", (
                String withThisTechno,
                String withDifferentCase,
                String withoutVersionType) -> {
            moduleBuilder.reset();
            if (StringUtils.isNotEmpty(withDifferentCase)) {
                moduleBuilder.withName(moduleBuilder.getName().toUpperCase());
            }
            if (StringUtils.isNotEmpty(withThisTechno)) {
                moduleBuilder.withTechno(technoBuilder.build());
            }
            if (StringUtils.isNotEmpty(withoutVersionType)) {
                moduleBuilder.withVersionType(null);
            }
        });

        Given("^an existing module with this template content?$", (String templateContent) -> {
            commonSteps.ensureUserAuthIsSet();
            testContext.responseEntity = moduleClient.create(moduleBuilder.build());
            assertCreated();
            templateBuilder.setContent(templateContent);
            moduleClient.addTemplate(templateBuilder.build(), moduleBuilder.build());
        });

        Given("^another template in this module with this content?$", (String templateContent) -> {
            templateBuilder.withName("template2").withFilename("template2.json").setContent(templateContent);
            moduleClient.addTemplate(templateBuilder.build(), moduleBuilder.build());
        });

        Given("^a module with a property \"([^\"]+)\" existing in versions: (.+)$",
                (String propertyName, String versions) -> {
                    addPropertyToBuilders(propertyName);
                    moduleBuilder.withTemplate(templateBuilder.build());
                    Arrays.stream(versions.split(", ")).forEach(version -> {
                        moduleBuilder.withVersion(version);
                        testContext.responseEntity = moduleClient.create(moduleBuilder.build());
                        assertCreated();
                        moduleBuilder.withVersionId(1);
                        testContext.responseEntity = moduleClient.addTemplate(templateBuilder.build(), moduleBuilder.build());
                        assertCreated();
                        moduleHistory.addModule();
                    });
                });

        When("^I( try to)? create this module$", (String tryTo) -> {
            testContext.responseEntity = moduleClient.create(moduleBuilder.build(), getResponseType(tryTo, ModuleIO.class));
        });

        Then("^the module is successfully created$", () -> {
            assertCreated();
            ModuleIO expectedModule = moduleBuilder.withVersionId(1).build();
            ModuleIO actualModule = (ModuleIO) testContext.getResponseBody();
            assertEquals(expectedModule, actualModule);
        });

        Then("^the module creation is rejected with a conflict error$", () -> {
            assertConflict();
        });

        Then("^the module creation is rejected with a not found error$", () -> {
            assertNotFound();
        });

        Then("^the module creation is rejected with a bad request error$", () -> {
            assertBadRequest();
        });
    }

    private void addPropertyToBuilders(String name) {
        addPropertyToBuilders(name, false);
    }

    private void addPropertyToBuilders(String name, boolean isPassword) {
        propertyBuilder.reset().withName(name);
        if (isPassword) {
            propertyBuilder.withIsPassword();
        }
        modelBuilder.withProperty(propertyBuilder.build());
        templateBuilder.withContent(propertyBuilder.toString());
    }
}
