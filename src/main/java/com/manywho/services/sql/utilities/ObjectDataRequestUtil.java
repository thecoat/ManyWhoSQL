package com.manywho.services.sql.utilities;

import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;

public class ObjectDataRequestUtil {
    public static boolean hasSearchFilter(ObjectDataRequest objectDataRequest) {
        return objectDataRequest.getListFilter() != null && objectDataRequest.getListFilter().getSearch() != null && !objectDataRequest.getListFilter().getSearch().isEmpty();
    }

    public static boolean hasListFilterId(ObjectDataRequest objectDataRequest) {
        return objectDataRequest.getListFilter() != null && objectDataRequest.getListFilter().getId() != null && !objectDataRequest.getListFilter().getId().isEmpty();
    }
}
