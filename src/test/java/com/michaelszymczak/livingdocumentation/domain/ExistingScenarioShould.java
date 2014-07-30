package com.michaelszymczak.livingdocumentation.domain;

import com.michaelszymczak.livingdocumentation.domain.ExistingFeature;
import com.michaelszymczak.livingdocumentation.domain.InvalidScenarioContentException;
import com.michaelszymczak.livingdocumentation.domain.Scenario;
import com.michaelszymczak.livingdocumentation.specificationprovider.ExistingFeatureBuilder;
import com.michaelszymczak.livingdocumentation.specificationprovider.ExistingScenarioBuilder;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class ExistingScenarioShould {

    @Test public void storeReferenceToTheWrappingFeatureSoThatOnaCanFindTheFeaturesFile() {
        ExistingFeature feature = ExistingFeatureBuilder.use().build();
        Scenario scenario = createScenarioPassingWrappingFeature(feature);
        Assert.assertSame(feature, scenario.getFeature());
    }

    @Test public void provideScenarioNameBasedOnTheContentPassedDuringCreation() {
        Scenario scenario = createScenarioFromContent(
                "Scenario: Foo title",
                "    Given bar"
        );

        Assert.assertEquals("Foo title", scenario.getName());
    }

    @Test public void provideScenarioNameIgnoringEmptyLinesAndTrailingSpaces() {
        Scenario scenario = createScenarioFromContent(
                "    ",
                "  Scenario: Bar title    ",
                "    Given bar"
        );

        Assert.assertEquals("Bar title", scenario.getName());
    }

    @Test public void treatUseScenarioTemplateAsScenarioNameAsWell() {
        Scenario scenario = createScenarioFromContent(
                "  Scenario Outline: Foo scenario outline name",
                "    Given bar"
        );

        Assert.assertEquals("Foo scenario outline name", scenario.getName());
    }

    @Test public void acceptEmptyScenarioName() {
        Scenario scenario = createScenarioFromContent("Scenario:");

        Assert.assertEquals("", scenario.getName());
    }

    @Test public void returnItsDataInJsonFormat() {
        Scenario scenario = ExistingScenarioBuilder.use().withWrappingFeature(
            ExistingFeatureBuilder.use().withPath("/path/to/Foo.feature").build()
        ).withContent("Scenario: Bar title", "    Given baz").build();

        Assert.assertEquals(
            "{\"name\":\"Bar title\",\"path\":\"/path/to/Foo.feature\",\"content\":[\"Scenario: Bar title\",\"    Given baz\"],\"result\":\"found\"}",
            scenario.toJson());
    }

    @Test public void provideOriginalContentOfTheScenarioSoThatItCanBeDisplayedInDocumentation() {
        assertContentOfTheScenarioPreserved(
                "Scenario: Foo title",
                "    Given bar"
        );
    }

    @Test(expected = InvalidScenarioContentException.class)
    public void throwExceptionIfNoScenarioLine() {
        createScenarioFromContent("Given foo");
    }

    @Test(expected = InvalidScenarioContentException.class)
    public void throwExceptionIfTooManyScenarioLines() {
        createScenarioFromContent(
            "Scenario: Foo",
            "Scenario: Bar"
        );
    }

    @Test(expected = InvalidScenarioContentException.class)
    public void throwExceptionIfBothScenarioAndScenarioOutlinePresent() {
        createScenarioFromContent(
            "Scenario: Foo",
            "Scenario Outline: Bar"
        );
    }

    @Test public void
    ignoreScenarioKeywordInsideMultilineQuotation() {
        assertContentOfTheScenarioPreserved(
                "Scenario: Foo",
                "  Given I have quotation as follows:",
                "  \"\"\"",
                "    Scenario: only a quote, not yet another scenario",
                "    Scenario Outline: only a quote, not yet another scenario",
                "\"\"\"",
                "  Then everything should be fine"
        );
    }

    @Test(expected = InvalidScenarioContentException.class) public void
    spotAProblemWhenFeatureKeywordOutsideQuotation() {
        assertContentOfTheScenarioPreserved(
                "Scenario: too many scenario keywords",
                "  Given I have quotation as follows:",
                "  \"\"\"",
                "    Scenario: only a quote, not yet another scenario",
                "  \"\"\"",
                "    Then everything should be fine",
                "Scenario: but this one should not be here, outside quotation",
                "  Given foo"
        );
    }

    private void assertContentOfTheScenarioPreserved(String... lines) {
        Scenario scenario = createScenarioFromContent(lines);
        Assert.assertEquals(Arrays.asList(lines), scenario.getContent());
    }

    private Scenario createScenarioFromContent(String... scenarioContent) {
        return ExistingScenarioBuilder.use().withContent(scenarioContent).build();
    }


    private Scenario createScenarioPassingWrappingFeature(ExistingFeature wrappingFeature) {
        return ExistingScenarioBuilder.use().withWrappingFeature(wrappingFeature).build();
    }
}