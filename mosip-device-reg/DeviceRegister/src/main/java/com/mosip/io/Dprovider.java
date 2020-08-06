package com.mosip.io;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.mosip.io.db.DataBaseAccess;
import com.mosip.io.pojo.DeviceRegisterDTO;
import com.mosip.io.util.ServiceUrl;
import com.mosip.io.util.Util;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Dprovider extends Util{
	
	public List<String> registerVedorWithMosip() {
		DeviceRegisterDTO deviceRegisterDTO =new DeviceRegisterDTO();
		JSONObject jsonData = readJsonData("/Request/deviceprovider.json");
		auditLog.info("Acutual Reqeust: "+jsonData.toJSONString());
		try {
			ObjectMapper mapper = new ObjectMapper();
			deviceRegisterDTO = mapper.readValue(jsonData.toJSONString(), DeviceRegisterDTO.class);
			deviceRegisterDTO.getRequest().setAddress(commonDataProp.get("address"));
			deviceRegisterDTO.getRequest().setCertificateAlias(commonDataProp.get("certificateAlias"));
			deviceRegisterDTO.getRequest().setContactNumber(commonDataProp.get("contactNumber"));
			deviceRegisterDTO.getRequest().setEmail(commonDataProp.get("email"));
			deviceRegisterDTO.getRequest().setIsActive(Boolean.valueOf(commonDataProp.get("isActive")));
			deviceRegisterDTO.getRequest().setVendorName(commonDataProp.get("vendorName"));
			String value=mapper.writeValueAsString(deviceRegisterDTO);
			auditLog.info("Updated Reqeust: "+value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String url = ServiceUrl.DEVICE_PROVIDER;
        RestAssured.baseURI = System.getProperty("baseUrl");
        Response api_response =
                given()
                        .cookie("Authorization", Util.cookies)
                        .contentType("application/json")
                        .body(deviceRegisterDTO)
                        .post(url);
        
        auditLog.info("Endpoint :"+url);
	    auditLog.info("Request  :"+deviceRegisterDTO);
	    auditLog.info("Response  :"+api_response.getBody().jsonPath().prettify());
	    
	    ReadContext ctx = JsonPath.parse(api_response.getBody().asString());
	    List<String> providerList= new ArrayList<>();
	    if(ctx.read("$.response") != null) {
	    	String deviceProderId = (String)ctx.read("$.response.id");
	    	String vendorName     = (String)ctx.read("$.response.vendorName");
	    	providerList.add(deviceProderId);
	    	providerList.add(vendorName);
	    }else {
	    	throw new RuntimeException("Please check "+System.getProperty("type")+".properties file");
	    }
        return providerList;
	}
	
	public boolean dbCheck(String type,String deviceProderId) {
		if(type==null || type.isEmpty())
			throw new RuntimeException("Please provide type value from Vm argument");
		boolean isPresent=false;
		switch(type) {
		case "Face":
			isPresent = isProviderIdPresentInDB(deviceProderId);
			break;
		case "Iris":
			isPresent = isProviderIdPresentInDB(deviceProderId);
			break;
		case "Finger":
			isPresent = isProviderIdPresentInDB(deviceProderId);
			break;
		case "Auth":
			isPresent = isProviderIdPresentInDB(deviceProderId);
				break;
			default:
				throw new RuntimeException("Invalid type : "+type+" is found!");
		}
		return isPresent;
	}

	private boolean isProviderIdPresentInDB(String deviceProderId) {
		boolean isPresent=false;
		DataBaseAccess db= new DataBaseAccess();
		String device_providerQuery = "Select * from master.device_provider where id="+"'"+deviceProderId+"'";
		String device_providerHistoryQuery = "Select * from master.device_provider_h where id="+"'"+deviceProderId+"'";
		if (db.getDbData(device_providerQuery, "masterdata").size()>0 && db.getDbData(device_providerHistoryQuery, "masterdata").size()>0)
			isPresent = true;
		return isPresent;
	}

}
