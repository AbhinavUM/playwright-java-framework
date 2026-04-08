package runner;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "stepdefs, hooks")
@ConfigurationParameter(
        key = "cucumber.plugin",
        value = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
public class TestRunner {
}