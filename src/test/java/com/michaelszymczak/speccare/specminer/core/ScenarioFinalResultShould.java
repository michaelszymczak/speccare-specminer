package com.michaelszymczak.speccare.specminer.core;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.michaelszymczak.speccare.specminer.core.ResultStatus.*;

public class ScenarioFinalResultShould {

    @Test public void
    produceResponseBasedOnScenarioFoundUsingProvidedText() throws IOException {
        ScenarioResultJudge result = new ScenarioResultJudge(
                repositoryFindingScenarioByGivenKey("Foo", ScenarioBuilder.use().withContent("Scenario: Foo scenario").build()),
                new AllwaysSameResultDeterminableStub(UNKNOWN)
        );

        ScenarioResponse response = result.createResponse("Foo");

        Assert.assertTrue(response.getContent().contains("\"content\":[\"Scenario: Foo scenario\"]"));
    }

    @Test public void
    useResultDirectlyFromScenarioFoundInScenarioRepositoryIfTheResultIsOtherThanFound() throws IOException {
        assertResponseWithSameStatusAsScenarioFromRepository(FAILED);
        assertResponseWithSameStatusAsScenarioFromRepository(NOT_FOUND);
        assertResponseWithSameStatusAsScenarioFromRepository(IGNORED);
        assertResponseWithSameStatusAsScenarioFromRepository(SKIPPED);
        assertResponseWithSameStatusAsScenarioFromRepository(AMBIGUOUS);
        assertResponseWithSameStatusAsScenarioFromRepository(UNKNOWN);
        assertResponseWithSameStatusAsScenarioFromRepository(PASSED);
    }

    @Test public void
    askExaminedScenarioResultsForFinalResultIfScenarioHasFoundResult() throws IOException {
        ScenarioResultJudge finalResult = new ScenarioResultJudge(
                repositoryReturningScenarioWithStatus(FOUND),
                DeterminableStub.buildReturningStatus(PASSED)
        );

        Assert.assertEquals(PASSED, finalResult.createResponse("some scenario").getStatus());
    }

    @Test public void
    useScenarioFullNameToFindItInExaminedScenarioResults() throws IOException {
        Map<String, ResultStatus> examinedScenarioStatuses = new HashMap<>();
        examinedScenarioStatuses.put("Bar", UNKNOWN);
        examinedScenarioStatuses.put("Foo Bar scenario", FAILED);


        ScenarioResultJudge result = new ScenarioResultJudge(
                repositoryFindingScenarioByGivenKey("Bar", ScenarioBuilder.use().withResult(FOUND).withName("Foo Bar scenario").build()),
                DeterminableStub.buildReturningStatusForScenarioName(examinedScenarioStatuses)
        );

        Assert.assertEquals(FAILED, result.createResponse("Bar").getStatus());
    }

    private void assertResponseWithSameStatusAsScenarioFromRepository(ResultStatus expectedResultStatus) throws IOException {
        ScenarioResultJudge finalResult = new ScenarioResultJudge(
                repositoryReturningScenarioWithStatus(expectedResultStatus),
                new AllwaysSameResultDeterminableStub(UNKNOWN)
        );

        Assert.assertEquals(expectedResultStatus, finalResult.createResponse("whatever").getStatus());
    }

    private Determinable repositoryReturningScenarioWithStatus(final ResultStatus scenarioResult) {
        return new Determinable() {
            @Override
            public Scenario determine(Scenario soughtScenario) throws IOException {
                return ScenarioBuilder.use().withResult(scenarioResult).build();
            }
        };
    }

    private Determinable repositoryFindingScenarioByGivenKey(final String key, final Scenario returnedScenario) {
        return new Determinable() {
            @Override
            public Scenario determine(Scenario soughtScenario) throws IOException {
                if (key.equals(soughtScenario.getName())) {
                    return returnedScenario;
                }
                throw new IOException(soughtScenario.getName() + " not found");
            }
        };
    }

}
