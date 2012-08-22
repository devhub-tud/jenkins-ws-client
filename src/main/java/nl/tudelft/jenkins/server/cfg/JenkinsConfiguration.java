package nl.tudelft.jenkins.server.cfg;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.JsonObject;

public class JenkinsConfiguration {

	private final JsonObject json;

	public JenkinsConfiguration(final JsonObject json) {

		checkNotNull(json, "JsonObject json should be non-null");
		isAcceptable(json);

		this.json = json;

	}

	public long getNumExecutors() {
		return json.get("numExecutors").getAsLong();
	}

	public void setNumExecutors(final int numExecutors) {
		json.addProperty("numExecutors", numExecutors);
	}

	@Override
	public String toString() {
		return json.toString();
	}

	private void isAcceptable(final JsonObject json) {

		checkArgument(json.has("numExecutors") && json.get("numExecutors").isJsonPrimitive() && json.get("numExecutors").getAsJsonPrimitive().isNumber(),
				"attribute numExecutors is not a number");

	}

}
