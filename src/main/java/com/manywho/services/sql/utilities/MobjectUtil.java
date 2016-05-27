package com.manywho.services.sql.utilities;

import com.manywho.sdk.api.run.elements.type.Property;

import java.util.List;
import java.util.Objects;

public class MobjectUtil {
    public static String getPrimaryKeyValue(String primaryKeyName, List<Property> properties) {
        for (Property prperty:properties) {
            if(Objects.equals(prperty.getDeveloperName(), primaryKeyName)) {
                return prperty.getContentValue();
            }
        }

        return "";
    }
}
