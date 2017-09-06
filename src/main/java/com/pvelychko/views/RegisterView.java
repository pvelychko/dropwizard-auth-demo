package com.pvelychko.views;

import io.dropwizard.views.View;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.indirect.FormClient;

public class RegisterView  extends View {
    private String callbackUrl;

    public RegisterView(Config config) {
        super("/register.mustache");
        this.callbackUrl = "/";
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }
}
