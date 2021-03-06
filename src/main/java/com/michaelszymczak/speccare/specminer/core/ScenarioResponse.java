package com.michaelszymczak.speccare.specminer.core;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.michaelszymczak.speccare.specminer.core.ResultStatus.*;

public class ScenarioResponse {

    private final Content content;
    private final ResultStatus status;

    // according to https://sites.google.com/site/gson :
    // The Gson instance does not maintain any state while invoking Json operations.
    // So, you are free to reuse the same object for multiple Json serialization and deserialization operations.
    private static final Gson GSON = new Gson();

    @Deprecated
    public ScenarioResponse(Scenario scenario, ResultStatus status) {
        this.content = new Content(scenario, status);
        this.status = status;
    }

    public ScenarioResponse(Scenario scenario) {
        this.content = new Content(scenario, scenario.getResult());
        this.status = scenario.getResult();
    }

    public String getContent() {
        return GSON.toJson(content);
    }

    public HttpStatus getHttpStatus() {
        if (AMBIGUOUS == status || UNKNOWN == status) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
        if (NOT_FOUND == status) {
            return HttpStatus.NOT_FOUND;
        }

        return HttpStatus.OK;
    }

    public ResultStatus getStatus() {
        return status;
    }

    private static class Content {
        @SuppressWarnings("unused") private final String name;
        @SuppressWarnings("unused") private final String path;
        @SuppressWarnings("unused") private final List<String> content;
        @SuppressWarnings("unused") private final String result;

        private Content(Scenario scenario, ResultStatus status) {
            name = scenario.getName();
            path = scenario.getFeaturePath();
            content = scenario.getContent();
            result = status.toString();
        }
    }
}
