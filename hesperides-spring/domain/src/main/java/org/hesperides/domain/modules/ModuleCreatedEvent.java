package org.hesperides.domain.modules;

import lombok.Value;

@Value
public class ModuleCreatedEvent {
    Module.Key moduleKey;
}
