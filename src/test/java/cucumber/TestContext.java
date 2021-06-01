package cucumber;

import apiEngine.Endpoints;

public class TestContext {
	private String BASE_URL = "https://bookstore.toolsqa.com";
	private Endpoints endPoints;
	
	public TestContext() {
		endPoints = new Endpoints(BASE_URL);
	}
	
	 public Endpoints getEndPoints() {
        return endPoints;
    }
}
