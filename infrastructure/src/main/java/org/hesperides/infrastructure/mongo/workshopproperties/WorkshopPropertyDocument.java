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
package org.hesperides.infrastructure.mongo.workshopproperties;

import lombok.Data;
import org.hesperides.domain.workshopproperties.entities.WorkshopProperty;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "workshopproperties")
public class WorkshopPropertyDocument {

    @Id
    private String key;
    private String value;
    private String keyValue;
    private int version;

    public static WorkshopPropertyDocument fromDomainInstance(WorkshopProperty domain) {
        WorkshopPropertyDocument doc = new WorkshopPropertyDocument();
        doc.setKey(domain.getKey());
        doc.setValue(domain.getValue());
        doc.setKeyValue(domain.getKeyValue());
        doc.setVersion(domain.getVersion());

        return doc;
    }

    public WorkshopPropertyView toWorkshopPropertyView() {
        return new WorkshopPropertyView(
                key, value, keyValue, version
        );
    }
}
