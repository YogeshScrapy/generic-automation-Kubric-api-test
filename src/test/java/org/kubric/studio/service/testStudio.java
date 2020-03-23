package org.kubric.studio.service;

import com.google.gson.JsonObject;
import io.restassured.response.ValidatableResponse;
import org.kubric.commonUtils.CSVParametersProvider;
import org.kubric.commonUtils.DataFileParameters;
import org.kubric.commonUtils.JSONFileReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class testStudio {


    String UrlToUpload = null;
    private static ValidatableResponse response;
    private static ValidatableResponse response2;
    private static ValidatableResponse response3;
    private static ValidatableResponse response4;
    private static ValidatableResponse response5;
    String responseJson;
    studioServiceRequestBuilder srb;
    JSONFileReader jReader;
    String Id_to_delete=null;



    @BeforeClass
    public void init() throws IOException {
        String env = System.getProperty("Environment");
        System.out.println("--------- Env name: " + env + " ---------");

        if (env.equalsIgnoreCase("Staging")) {
            srb = new studioServiceRequestBuilder("/resources/config-files/staging/InferenceService.properties");
        } else if (env.equalsIgnoreCase("Production")) {
            srb = new studioServiceRequestBuilder("/resources/config-files/Production/InferenceService.properties");
        }
        jReader = new JSONFileReader();

    }


    @Test(enabled = true, priority = 0, description = "Test Case - To create a Shot", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "createshot.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void createShot(String uri) {
        response = srb.sendformRequestCreateShot(uri);
        String name = response.extract().jsonPath().getString("data.name");
        Id_to_delete = response.extract().jsonPath().getString("data.uid");
        String status = response.extract().jsonPath().getString("status");

        Assert.assertTrue(name.equals("AutomationTestShot"));
        Assert.assertTrue(status.equals("200"));

    }


    @Test(enabled = true, priority = 0, description = "Test Case - To get a Shot", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "getShot.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void getShot(String uri)
    {
        response = srb.getRequest(uri);
        String name = response.extract().jsonPath().getString("data.name");
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));


    }


    @Test(enabled = true, priority = 0, description = "Test Case - To find a Shot", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "findShots.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void findShot(String uri)
    {
        response = srb.getRequest(uri);
        String name = response.extract().jsonPath().getString("data.name");
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));

    }



    @Test(enabled = true, priority = 1, description = "Test Case - To Delete a Shot", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "deleteShot.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void deleteShot(String uri)
    {
        uri = uri+Id_to_delete+"?workspace_id=b98b4bf4-989c-49cd-9597-b287cb8436df";
        response = srb.delteRequest(uri);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));

    }


    @Test(enabled = true, priority = 0, description = "Test Case - To get a Shot by Id and Uid", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "getShotByUidAndId.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void getShotByIdAndUid(String uri)
    {
        response = srb.getRequest(uri);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));

    }


    @Test(enabled = true, priority = 0, description = "Test Case - To update a Shot by Id and Uid", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "updateShot.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void updateShot(String uri)
    {
        response = srb.patchformRequestCreateShot(uri);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));

    }

    @Test(enabled = true, priority = 0, description = "Test Case - To share a campaign inside testing workspace with teams", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "shareshot.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void shareshot(String uri) throws FileNotFoundException {
        JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Storyboard-Service/shreShot.json");
        JsonObject jsonReqBody = srb.createRequestBody(jsonFileObject);
        response = srb.patchsharecamps(uri, jsonReqBody);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));


    }
    @Test(enabled = true, priority = 0, description = "Test Case - To unshare a campaign inside testing workspace with teams", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "Unshareshot.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void unshareshot(String uri) throws FileNotFoundException {
        JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Storyboard-Service/unshareshot.json");
        JsonObject jsonReqBody = srb.createRequestBody(jsonFileObject);
        response = srb.patchsharecamps(uri, jsonReqBody);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));


    }


    @Test(enabled = true, priority = 0, description = "Test Case - To create a campaign inside testing workspace", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "createcampaigns.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void createcampaigns(String uri, String content_type)
    {
        response = srb.sendCreateCampaigns(uri,content_type);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));


    }

    @Test(enabled = true, priority = 0, description = "Test Case - To get a campaign inside testing workspace by campaign id", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "createcampaigns.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void getcampaigns(String uri, String content_type)
    {
        response = srb.getassetFilter(uri);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));


    }


    @Test(enabled = true, priority = 0, description = "Test Case - To share a campaign inside testing workspace with teams", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "shareCampaigns.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void sharecampaigns(String uri) throws FileNotFoundException {
        JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Storyboard-Service/shareCampaigns.json");
        JsonObject jsonReqBody = srb.createRequestBody(jsonFileObject);
        response = srb.patchsharecamps(uri, jsonReqBody);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));


    }
    @Test(enabled = true, priority = 0, description = "Test Case - To unshare a campaign inside testing workspace with teams", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "unshareCampigns.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void unsharecampaigns(String uri) throws FileNotFoundException {
        JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Storyboard-Service/unshareCampaigns.json");
        JsonObject jsonReqBody = srb.createRequestBody(jsonFileObject);
        response = srb.patchsharecamps(uri, jsonReqBody);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));


    }

    @Test(enabled = true, priority = 0, description = "Test Case - To find a campaign inside testing workspace", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "findCampaigns.csv", path = "/resources/input-data/Sotoryboard-Service")
    public void findcampaigns(String uri) throws FileNotFoundException {

        response = srb.getRequest(uri);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));


    }
}

