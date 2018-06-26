package org.hesperides.presentation.io;

import com.google.gson.annotations.SerializedName;
import lombok.Value;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;

@Value
public class WorkshopPropertyOutput {
    String key;
    String value;
    @SerializedName("key_value")
    String keyValue;
    @SerializedName("v")
    int version;

    public static WorkshopPropertyOutput fromWorkshopPropertyView(WorkshopPropertyView view) {
        return new WorkshopPropertyOutput(
                view.getKey(),
                view.getValue(),
                view.getKeyValue(),
                view.getVersion()
        );
    }
}
