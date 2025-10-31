package api;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		packages("api");
		// register resources and filters explicitly
		register(CustomerResource.class);
		register(AccountResource.class);
		register(TransactionResource.class);
		register(AuthResource.class);
		register(CorsFilter.class);
	}
}
