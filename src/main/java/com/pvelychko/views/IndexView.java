package com.pvelychko.views;

import java.util.Optional;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;

import io.dropwizard.views.View;

public class IndexView extends View {
    private final ProfileManager<CommonProfile> pm;

    public IndexView(ProfileManager<CommonProfile> pm) {
        super("/index.mustache");
        this.pm = pm;
    }
    
    public Optional<CommonProfile> getUser() {
        return pm.get(true);
    }
}
