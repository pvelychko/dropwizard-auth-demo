package com.pvelychko;

import com.pvelychko.auth.AbstractAuthenticator;
import com.pvelychko.auth.TokenAuthenticator;
import com.pvelychko.config.AppConfiguration;
import com.pvelychko.model.LoginAttempt;
import com.pvelychko.model.User;
import com.pvelychko.resources.RegistrationResouce;
import com.pvelychko.resources.UserResource;
import com.pvelychko.resources.ViewsResource;
import com.pvelychko.store.UserStore;
import com.pvelychko.store.jdbc.UserStoreJdbcImpl;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;
import org.pac4j.core.config.Config;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.dropwizard.Pac4jBundle;
import org.pac4j.dropwizard.Pac4jFactory;
import org.pac4j.http.client.indirect.FormClient;

public class AuthApplication extends Application {
    public static void main(String[] args) throws Exception {
        new AuthApplication().run(args);
    }

    private final HibernateBundle<AppConfiguration> hibernateBundle =
        new HibernateBundle<AppConfiguration>(User.class, LoginAttempt.class) {
            @Override
            public DataSourceFactory getDataSourceFactory(AppConfiguration configuration) {
                return configuration.getDatasourceFactory();
            }
        };

    private final Pac4jBundle<AppConfiguration> pac4jBundle = new Pac4jBundle<AppConfiguration>() {
        @Override
        public Pac4jFactory getPac4jFactory(AppConfiguration configuration) {
            return configuration.getPac4jFactory();
        }
    };

    @Override
    public void initialize(Bootstrap bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(pac4jBundle);
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new AssetsBundle("/static/css","/css", null, "css"));
        bootstrap.addBundle(new AssetsBundle("/static/fonts","/fonts", null, "fonts"));
        bootstrap.addBundle(new AssetsBundle("/static/images","/images", null, "images"));
        bootstrap.addBundle(new AssetsBundle("/static/js", "/js", null, "js"));
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        final UserStore userStore = new UserStoreJdbcImpl(hibernateBundle.getSessionFactory());

        AbstractAuthenticator basicAuthenticator = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(AbstractAuthenticator.class, SessionFactory.class, hibernateBundle.getSessionFactory());
        final AuthDynamicFeature basicCredentialAuthFilter = new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(basicAuthenticator)
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter());

        environment.jersey().register(basicCredentialAuthFilter);
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(new UserResource(userStore));
        environment.jersey().register(new RegistrationResouce(userStore));

        environment.servlets().setSessionHandler(new SessionHandler());
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(pac4jBundle.getConfig()).to(Config.class);
            }
        });

        final FormClient formClient = new FormClient();
        formClient.setLoginUrl("/login");
        final Authenticator<UsernamePasswordCredentials> authenticator = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(TokenAuthenticator.class,  SessionFactory.class,
                        hibernateBundle.getSessionFactory());
        formClient.setAuthenticator(authenticator);
        pac4jBundle.getConfig().getClients().getClients().add(formClient);

        environment.jersey().register(new ViewsResource(pac4jBundle.getConfig(), hibernateBundle.getSessionFactory()));
    }
}
