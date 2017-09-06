package com.pvelychko.views;

import io.dropwizard.views.View;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.indirect.FormClient;

public class LoginView extends View {
    private String callbackUrl;

    public LoginView(Config config) {
        super("/login.mustache");
        this.callbackUrl = config.getClients().findClient(FormClient.class).getCallbackUrl();
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }
}
