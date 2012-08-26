package nl.tudelft.jenkins.jobs;

public interface Job {

	String getName();

	void setScmUrl(String string);

	String asXml();

}
