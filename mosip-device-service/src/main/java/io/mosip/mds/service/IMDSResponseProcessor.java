package io.mosip.mds.service;

import io.mosip.mds.dto.CaptureResponse;
import io.mosip.mds.dto.DeviceDto;
import io.mosip.mds.dto.MdsResponse;
import io.mosip.mds.dto.TestRun;
import io.mosip.mds.util.Intent;
import io.mosip.mds.dto.TestDefinition;

public interface IMDSResponseProcessor {
    
    public String getSpecVersion();

    public String processResponse(TestRun run, TestDefinition test, DeviceDto device, Intent op);
    
    public String getRenderContent(Intent op, String responseData);
    
    public CaptureResponse getCaptureResponse(Intent intent, String responseData);

	public MdsResponse[] getMdsDecodedResponse(Intent intent, String responseData);

}