package com.manywho.services.sql;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.manywho.sdk.services.types.TypeProvider;
import com.manywho.services.sql.managers.DataManager;
import com.manywho.services.sql.managers.DescribeManager;
import com.manywho.services.sql.managers.MetadataManager;
import com.manywho.services.sql.services.*;
import com.manywho.services.sql.types.RawTypeProvider;
import com.manywho.services.sql.utilities.MobjectUtil;

public class ApplicationSqlModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MetadataManager.class);
        bind(DescribeService.class);
        bind(DescribeManager.class);
        bind(DataManager.class);
        bind(MetadataService.class);
        bind(DataService.class);
        bind(QueryStrService.class);
        bind(QueryParameterService.class).in(Singleton.class);
        bind(TypeProvider.class).to(RawTypeProvider.class);
        bind(MobjectUtil.class);
    }
}
