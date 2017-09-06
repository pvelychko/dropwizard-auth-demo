package com.pvelychko.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.pac4j.dropwizard.Pac4jFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AppConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory datasourceFactory = new DataSourceFactory();

    @JsonProperty("pac4j")
    private Pac4jFactory pac4jFactory = new Pac4jFactory();

    public DataSourceFactory getDatasourceFactory() {
        return datasourceFactory;
    }

    public Pac4jFactory getPac4jFactory() {
        return pac4jFactory;
    }
}
