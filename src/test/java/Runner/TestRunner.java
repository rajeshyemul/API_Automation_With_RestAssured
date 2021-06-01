package Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(	plugin = {"pretty",
        					"html:target/cucumber-reports/CucumberTestReport.html",
        					"json:target/cucumber-reports/CucumberTestReport.json"
							},
					features = {"src/test/resources/FunctionalTest"}, 
					glue  = {"StepsDefinitions"},
					monochrome = true)

public class TestRunner extends AbstractTestNGCucumberTests{
	

}
