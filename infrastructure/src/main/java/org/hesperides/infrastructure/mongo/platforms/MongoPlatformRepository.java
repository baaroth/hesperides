package org.hesperides.infrastructure.mongo.platforms;

import org.hesperides.infrastructure.mongo.platforms.documents.PlatformDocument;
import org.hesperides.infrastructure.mongo.platforms.documents.PlatformKeyDocument;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.hesperides.domain.framework.Profiles.FAKE_MONGO;
import static org.hesperides.domain.framework.Profiles.MONGO;

@Profile({MONGO, FAKE_MONGO})
@Repository
public interface MongoPlatformRepository extends MongoRepository<PlatformDocument, String> {

    Optional<PlatformDocument> findOptionalByKey(PlatformKeyDocument platformKeyDocument);

    List<PlatformDocument> findAllByKeyApplicationNameLike(String input);

    void deleteByKey(PlatformKeyDocument key);

    List<PlatformDocument> findAllByKeyApplicationNameLikeAndKeyPlatformNameLike(String applicationName, String platformName);

    List<PlatformDocument> findAllByKeyApplicationName(String applicationName);

    List<PlatformDocument> findAllByDeployedModulesNameAndDeployedModulesVersionAndDeployedModulesWorkingCopy(String moduleName, String moduleVersion, boolean isWorkingCopy);
}
