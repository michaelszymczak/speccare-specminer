package com.michaelszymczak.speccare.specminer.domain;


import java.io.IOException;

public interface PartialResult {
    public ResultStatus getResult(String scenarioName) throws IOException;
}
