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
package org.hesperides.infrastructure.mongo.templatecontainer;

import lombok.Data;
import org.hesperides.domain.templatecontainer.entities.Model;
import org.hesperides.domain.templatecontainer.queries.ModelView;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class ModelDocument {

    private List<PropertyDocument> properties;
    private List<IterablePropertyDocument> iterableProperties;

    public static ModelDocument fromDomainInstance(Model model) {
        ModelDocument modelDocument = new ModelDocument();
        if (model != null) {
            modelDocument.setProperties(PropertyDocument.fromDomainInstances(model.getProperties()));
            modelDocument.setIterableProperties(IterablePropertyDocument.fromDomainInstances(model.getIterableProperties()));
        }
        return modelDocument;
    }

    public ModelView toModelView() {
        return new ModelView(
                PropertyDocument.toPropertyViews(properties),
                IterablePropertyDocument.toIterableProperyViews(iterableProperties)
        );
    }
}
