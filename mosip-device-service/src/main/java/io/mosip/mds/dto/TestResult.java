package io.mosip.mds.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.mosip.mds.dto.postresponse.RequestInfoDto;
import io.mosip.mds.dto.postresponse.ValidationResult;
import org.springframework.format.annotation.DateTimeFormat;


public class TestResult {

        @Override
    public String toString() {
        return String.format(
                "\n Test Result"
                +"\n Run Id "+runId
                        +"\n Test Id " + testId
                        +"\n Executed On " +executedOn.toString()
                        +"\n Summary " +summary
                        +"\n Request Data " + requestData
                        +"\n Response Data " + responseData
                        +"\n Validation Results \n" +validationResults.toString()
                        +"\n Render Content " +renderContent
        );
    }

    public TestResult() {  }

    public TestResult(String runId, String testId, String description) {
        this.runId = runId;
        this.testId = testId;
        this.summary = description;
        this.startedOn = new Date();
        this.currentState = "Execution initiated";
    }

    public String runId;

    public String testId;

    public Date executedOn;

    public String summary;
    
    public String requestData;

    public String responseData;

    public List<ValidationResult> validationResults = new ArrayList<>();

    public String renderContent;

    public Date startedOn;

    public String currentState;
    
    public String streamUrl;
}
