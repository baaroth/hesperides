package org.hesperides.infrastructure.elasticsearch.modules;

import lombok.Data;
import org.hesperides.domain.modules.entities.Module;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import static org.hesperides.domain.Profiles.ELASTICSEARCH;

@Profile(ELASTICSEARCH)
@Document(indexName = "#{@indexName}", type = "module")
@Data
public class ModuleDocument {
    @Id
    String id;

    String name;
    String version;
    Module.Type versionType;
    Long versionId;
}
