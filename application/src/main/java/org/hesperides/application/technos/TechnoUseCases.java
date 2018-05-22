package org.hesperides.application.technos;

import org.hesperides.domain.security.User;
import org.hesperides.domain.technos.commands.TechnoCommands;
import org.hesperides.domain.technos.entities.Techno;
import org.hesperides.domain.technos.exception.DuplicateTechnoException;
import org.hesperides.domain.technos.exception.TechnoNotFoundException;
import org.hesperides.domain.technos.queries.TechnoQueries;
import org.hesperides.domain.technos.queries.TechnoView;
import org.hesperides.domain.templatecontainer.entities.Template;
import org.hesperides.domain.templatecontainer.entities.TemplateContainer;
import org.hesperides.domain.templatecontainer.queries.TemplateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Ensemble des cas d'utilisation liés à l'agrégat Techno
 */
@Component
public class TechnoUseCases {

    private final TechnoCommands commands;
    private final TechnoQueries queries;

    @Autowired
    public TechnoUseCases(TechnoCommands commands, TechnoQueries queries) {
        this.commands = commands;
        this.queries = queries;
    }

    /**
     * Crée la techno si elle n'existe pas
     * Ajoute un template à cette techno
     *
     * @param technoKey
     * @param template
     * @param user
     */
    public void addTemplate(TemplateContainer.Key technoKey, Template template, User user) {
        if (!queries.technoExists(technoKey)) {
            Techno techno = new Techno(technoKey, null);
            commands.createTechno(techno, user);
        }
        commands.addTemplate(technoKey, template, user);
    }

    public Optional<TemplateView> getTemplate(TemplateContainer.Key technoKey, String templateName) {
        return queries.getTemplate(technoKey, templateName);
    }

    public void deleteTechno(TemplateContainer.Key technoKey, User user) {
        if (!queries.technoExists(technoKey)) {
            throw new TechnoNotFoundException(technoKey);
        }
        commands.deleteTechno(technoKey, user);
    }

    public List<TemplateView> getTemplates(TemplateContainer.Key technoKey) {
        if (!queries.technoExists(technoKey)) {
            throw new TechnoNotFoundException(technoKey);
        }
        return queries.getTemplates(technoKey);
    }

    public TechnoView releaseTechno(TemplateContainer.Key existingTechnoKey, User user) {
        TemplateContainer.Key newTechnoKey = new TemplateContainer.Key(existingTechnoKey.getName(), existingTechnoKey.getVersion(), TemplateContainer.VersionType.release);
        if (queries.technoExists(newTechnoKey)) {
            throw new DuplicateTechnoException(newTechnoKey);
        }

        Optional<TechnoView> technoView = queries.getTechno(existingTechnoKey);
        if (!technoView.isPresent()) {
            throw new TechnoNotFoundException(existingTechnoKey);
        }

        Techno existingTechno = technoView.get().toDomainInstance();
        Techno technoRelease = new Techno(newTechnoKey, existingTechno.getTemplates());

        commands.createTechno(technoRelease, user);
        return queries.getTechno(newTechnoKey).get();
    }
}
