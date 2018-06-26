package org.hesperides.presentation.io;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;

import lombok.Value;
import org.hesperides.domain.workshopproperties.entities.WorkshopProperty;
import org.hibernate.validator.constraints.NotEmpty;

@Value
public class WorkshopPropertyUpdateInput {
    @NotNull
    @NotEmpty
    String value;
    @NotNull
    @DecimalMin("1")
    Integer version;

    public WorkshopProperty toDomainInstance(String key) {
        return new WorkshopProperty(key, value, null, version);
    }
}
