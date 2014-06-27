package com.michaelszymczak.livingdocumentation.specificationprovider;


import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

public class NotFoundScenarioShould {

    @Test public void beCreatedUsingFactoryMethod() {
        Assert.assertSame(NotFoundScenario.class, Scenario.getNotFound().getClass());
    }

    @Test public void beOfTypeOfScenario() {
        Scenario s = Scenario.getNotFound();
    }

    @Test public void beAlwaysTheSameInstanceAsItIsImmutableObject() {
        Assert.assertSame(Scenario.getNotFound(), Scenario.getNotFound());
    }

    @Test public void presetItselfAsNotFoundScenario() {
        NotFoundScenario scenario = Scenario.getNotFound();

        Assert.assertEquals("Scenario not found", scenario.getName());
        Assert.assertEquals(0, scenario.getContent().size());
    }

    @Test public void haveNotFoundFeatureAsItsFeatureObject() {
        NotFoundScenario scenario = Scenario.getNotFound();
        Assert.assertSame(Feature.getNotFound(), scenario.getFeature());
    }
}