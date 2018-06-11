package org.hesperides.tests.bdd.technos.contexts;

import cucumber.api.java8.En;
import org.hesperides.domain.templatecontainer.entities.TemplateContainer;
import org.hesperides.presentation.io.TechnoIO;
import org.hesperides.presentation.io.TemplateIO;
import org.hesperides.tests.bdd.CucumberSpringBean;
import org.hesperides.tests.bdd.technos.TechnosSamples;
import org.hesperides.tests.bdd.templatecontainer.TemplateSamples;
import org.springframework.http.ResponseEntity;

public class TechnoContext extends CucumberSpringBean implements En {

    TemplateContainer.Key technoKey;

    public TechnoContext() {
        Given("^an existing techno$", () -> {
            TemplateIO templateInput = TemplateSamples.getTemplateInputWithDefaultValues();
            TechnoIO technoInput = TechnosSamples.getTechnoWithDefaultValues();
            createTechno(technoInput, templateInput);
        });

    }

    public TemplateContainer.Key getTechnoKey() {
        return technoKey;
    }

    public String getTechnoURI() {
        return String.format("/templates/packages/%s/%s/%s", technoKey.getName(), technoKey.getVersion(), technoKey.getVersionType());
    }

    public String getTemplatesURI() {
        return getTechnoURI() + "/templates";
    }

    public String getTemplateURI(String templateName) {
        return getTemplatesURI() + "/" + templateName;
    }

    public ResponseEntity<TemplateIO> retrieveExistingTemplate() {
        return retrieveExistingTemplate(TemplateSamples.DEFAULT_TEMPLATE_NAME);
    }

    public ResponseEntity<TemplateIO> retrieveExistingTemplate(String name) {
        return rest.getTestRest().getForEntity(getTemplateURI(name), TemplateIO.class);
    }

    public ResponseEntity<TemplateIO> createTechno(TechnoIO technoInput, TemplateIO templateInput) {
        ResponseEntity<TemplateIO> response = rest.getTestRest().postForEntity(
                "/templates/packages/{technoName}/{technoVersion}/workingcopy/templates",
                templateInput,
                TemplateIO.class,
                technoInput.getName(),
                technoInput.getVersion());
        technoKey = new TemplateContainer.Key(technoInput.getName(), technoInput.getVersion(), technoInput.isWorkingCopy() ? TemplateContainer.VersionType.workingcopy : TemplateContainer.VersionType.release);
        return response;
    }

    public ResponseEntity<TemplateIO> addTemplateToExistingTechno(String templateName) {
        TemplateIO templateInput = TemplateSamples.getTemplateInputWithName(templateName);
        TechnoIO technoInput = TechnosSamples.getTechnoFromTechnoKey(technoKey);
        return createTechno(technoInput, templateInput);
    }

    public ResponseEntity<TemplateIO> addTemplateToExistingTechno(TemplateIO templateInput) {
        TechnoIO technoInput = TechnosSamples.getTechnoFromTechnoKey(technoKey);
        return createTechno(technoInput, templateInput);
    }

    public ResponseEntity<TechnoIO> releaseTechno() {
        return rest.getTestRest().postForEntity("/templates/packages/create_release?techno_name={technoName}&techno_version={technoVersion}",
                null, TechnoIO.class, technoKey.getName(), technoKey.getVersion());
    }

    public String getNamespace() {
        return "packages#" + technoKey.getName() + "#" + technoKey.getVersion() + "#" + technoKey.getVersionType().name().toUpperCase();
    }
}
