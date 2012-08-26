package nl.tudelft.jenkins.guice;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import org.jclouds.Context;
import org.jclouds.ContextBuilder;
import org.jclouds.jenkins.v1.JenkinsApi;
import org.jclouds.jenkins.v1.JenkinsAsyncApi;
import org.jclouds.jenkins.v1.features.JobApi;
import org.jclouds.rest.RestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class JenkinsWsClientGuiceModule extends AbstractModule {

	private static final Logger LOG = LoggerFactory.getLogger(JenkinsWsClientGuiceModule.class);

	private final RestContext<JenkinsApi, JenkinsAsyncApi> jenkinsRestContext;

	public JenkinsWsClientGuiceModule() {
		this("http://dea.hartveld.com/jenkins/");
	}

	public JenkinsWsClientGuiceModule(final String endpoint) {

		LOG.trace("Creating new Jenkins WS Client Guice module with endpoint {} ...", endpoint);

		checkArgument(!isEmpty(endpoint), "endpoint must be non-empty");

		jenkinsRestContext = buildJenkinsRestContext();

	}

	private RestContext<JenkinsApi, JenkinsAsyncApi> buildJenkinsRestContext() {

		final Context context = ContextBuilder.newBuilder("jenkins").endpoint("http://dea.hartveld.com/jenkins/").build();

		@SuppressWarnings("unchecked")
		final RestContext<JenkinsApi, JenkinsAsyncApi> jenkinsRestContext = (RestContext<JenkinsApi, JenkinsAsyncApi>) context;

		return jenkinsRestContext;

	}

	@Override
	protected void configure() {

	}

	@Provides
	public RestContext<JenkinsApi, JenkinsAsyncApi> getRestContext() {
		return jenkinsRestContext;
	}

	@Provides
	public JenkinsApi getJenkinsApi() {
		return jenkinsRestContext.getApi();
	}

	@Provides
	public JobApi getJobApi() {
		return jenkinsRestContext.getApi().getJobApi();
	}

}
