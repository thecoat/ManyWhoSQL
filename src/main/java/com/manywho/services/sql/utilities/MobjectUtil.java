package com.manywho.services.sql.utilities;

import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.services.PrimaryKeyService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MobjectUtil {
    private PrimaryKeyService primaryKeyService;

    @Inject
    public MobjectUtil(PrimaryKeyService primaryKeyService) {
        this.primaryKeyService = primaryKeyService;
    }

    public String getPrimaryKeyValue(String primaryKeyName, List<Property> properties) {
        for (Property prperty:properties) {
            if(Objects.equals(prperty.getDeveloperName(), primaryKeyName)) {
                HashMap<String, String> primaryKeys = new HashMap<>();
                primaryKeys.put("id", prperty.getContentValue());

                return primaryKeyService.serializePrimaryKey(primaryKeys);
            }
        }

        return "";
    }
}
