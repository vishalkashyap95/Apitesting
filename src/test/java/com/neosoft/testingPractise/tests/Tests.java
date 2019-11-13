package com.neosoft.testingPractise.tests;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
import com.neosoft.testingPractise.Apitesting;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.*;

public class Tests {
    Apitesting at = new Apitesting();
    Properties prop = new Properties();
    String sJsonFile = "";
    String sJsonPathFile = "";

    @BeforeSuite
    public void readProperties() {
        try {
            System.out.println("Reading properties file...");
            prop.load(at.readProperty("Config.properties"));
            System.out.println(prop.getProperty("propMessage"));
            sJsonFile = at.readFile("testJsonFile.json");
            sJsonPathFile = at.readFile("testJsonPath.json");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("FAILED to read properties file...");
        }
    }

    @Test(enabled = false)
    public void testUserApi() {
        System.out.println("\n------------Running test - testUserApi()------------\n");
        String url = prop.getProperty("apiFixerBaseURL") + prop.getProperty("access_key");
        System.out.println("URL - " + url + "\n");
        String urlContentAfterReading = at.openUrlConAndGetContent(url);
        System.out.println(urlContentAfterReading);
    }

    @Test(enabled = false)
    public void testReadAndAccessJsonObjectsFromFile() {
        System.out.println("\n------------Running test - testReadJsonFile()------------\n");

        // Reading JSON File
//        String jsonFile = at.readFile("testJsonFile.json");
        System.out.println(sJsonFile);
        Assert.assertTrue(!sJsonFile.equals(""), "FAILED to read json file.");

        // Converting String File into Json Object
        try {
            // Accessing Entire Json Object
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(sJsonFile);
            System.out.println("JSON OBJECT" + jsonObject);

            // Accessing Quiz object from entire json object
            JSONObject quizObj = (JSONObject) jsonObject.get("quiz");
            System.out.println("Quiz Object : " + quizObj);

            // Accessing Sport Object from entire quiz object
            JSONObject sportObj = (JSONObject) quizObj.get("sport");
            System.out.println("Sport Object : " + sportObj);

            // Accessing q1 object from Sport object
            JSONObject sportQ1Object = (JSONObject) sportObj.get("q1");
            Set sets = new HashSet(sportQ1Object.keySet());
            System.out.println("hahahahah : " + sets);
            System.out.println(sportQ1Object);

            JSONArray sportOptions = (JSONArray) sportQ1Object.get("options");

            System.out.println("Size of SportOptions : " + sportOptions.size());
            for (int i = 0; i < sportOptions.size(); i++) {
                System.out.println("Sport Options : " + sportOptions.get(i));
                Assert.assertTrue((sportOptions.get(i) instanceof String), "FAILED to verify the Type of data in Sport Options.");
            }
        } catch (Exception e) {
            System.out.println("Exception caught in testReadJsonFile() method : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void testReadAndAccessJsonViaJsonPath() {
        try {
//            String sJsonFile = at.readFile("testJsonFile.json");
            System.out.println(sJsonFile);

            // Extract keys form q1 node
            ArrayList sportQ1Arr = JsonPath.parse(sJsonFile).read("$..sport.q1");
            System.out.println("Sport Q1 : " + sportQ1Arr);

            JSONObject jObject = (JSONObject) new JSONParser().parse(sJsonFile);
            System.out.println(jObject);
            System.out.println(jObject.keySet());

            // Reading JsonArray, storing it into List
            List<String> sportQ1Options = JsonPath.parse(sJsonFile).read("$..sport.q1.options[*]");

            // Printing Size, accessing each element from List and Asserting data type of each element.
            System.out.println(sportQ1Options.size());
            for (int i = 0; i < sportQ1Options.size(); i++) {
                System.out.println(sportQ1Options.get(i));
                Assert.assertTrue((sportQ1Options.get(i) instanceof String), "FAILED to validate the Data type of Sport Q1 Options.");
            }

//            String jsonPathFile = at.readFile("testJsonPath.json");
            System.out.println(sJsonPathFile);

            // Find highest revenue unique revenue
            ArrayList<Integer> highestRevenue = JsonPath.parse(sJsonPathFile).read("$[*]['box office']");
            TreeSet<Integer> uniqueValue = new TreeSet<Integer>(highestRevenue);
            for (int i = 0; i < highestRevenue.size(); i++) {
                System.out.println(highestRevenue.get(i));
//                uniqueValue.add(highestRevenue.get(i));
            }
            System.out.println("Size of Unique value : " + uniqueValue.size());
            System.out.println(uniqueValue);

//            // Trying filters
//            Filter idGreaterThan2 = Filter.filter(Criteria.where("id").gt(2));
//            List<Map<String, Integer>> ret = JsonPath.parse(jsonPathFile).read("$.['id']", idGreaterThan2);
//            for (int i = 0; i < ret.size(); i++) {
//                System.out.println(ret.get(i));
//            }
        } catch (Exception e) {
            System.out.println("Exception caught in testReadAndAccessJsonViaJsonPath() test : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void testAllKeysInObjects() throws Exception {

        // Extract values form sJsonFile
        JSONObject entireJsonObject = (JSONObject) new JSONParser().parse(sJsonFile);
//        JSONObject quizObject = (JSONObject) entireJsonObject.get("quiz");
//        System.out.println("Quiz Object : " + quizObject);

        String flattedJson = JsonFlattener.flatten(entireJsonObject.toJSONString());
        System.out.println(flattedJson);

        // Converting Flatted JSON string to JSONObject
        JSONObject flattedJsonObject = (JSONObject) new JSONParser().parse(flattedJson);
        System.out.println(flattedJsonObject.get("quiz.maths.q2.options[0]"));
        System.out.println(flattedJsonObject.get("quiz.maths.q2.options[0]").getClass());
        System.out.println(flattedJsonObject.get("quiz.maths.q2.options[0]") instanceof Long);
        System.out.println(flattedJsonObject.get("quiz.maths.q2.options[0]") instanceof String);

    }

    @Test
    public void testVerifyAllKeysAndValuesFromObject() throws Exception {
        // Check keys from returned object
        List<Map<String, Object>> filteredObjects = at.getKeyValueObjectByExpression(sJsonFile, "$..sport.q1");
        System.out.println("Set of Keys from Filtered Objects : " + filteredObjects.iterator().next().keySet());
        System.out.println("Does filtered object keySet contains 'answer'? : " + filteredObjects.iterator().next().containsKey("answer"));
        System.out.println("Does filtered object keySet contains 'question'? : " + filteredObjects.iterator().next().containsKey("question"));
        System.out.println("Does filtered object keySet contains 'options'? : " + filteredObjects.iterator().next().containsKey("options"));

        // Check Values from above returned objects
        List<Object> values = new ArrayList<Object>(filteredObjects.listIterator().next().values());
        System.out.println("Does filtered object Values contains 'Which one is correct team name in NBA'? : " + values.contains("Which one is correct team name in NBA?"));
        System.out.println("Does filtered object Values contains 'Huston Rocket'? : " + values.contains("Huston Rocket"));

        // Get and Check the data type of above values
        for (Object value : values) {
            System.out.println(value.getClass());
        }

        // Count the number of objects
        List<Map<String, Object>> objectsUnderRoot = JsonPath.read(sJsonFile, "$..sport.q1");
        System.out.println("count of objects under root : " + objectsUnderRoot.iterator().next().size());
        List<Map<String, Object>> objectsUnderRoot1 = JsonPath.read(sJsonFile, "$..sport..options");
        System.out.println("count of objects1 under root : " + objectsUnderRoot.iterator().next().size());


        // Fetch the JSONArray object
        ArrayList jsonArrayObject = new ArrayList(at.getListOfObjectsByExpression(sJsonFile, "$..sport..options[*]"));
        for (int i = 0; i < jsonArrayObject.size(); i++) {
            System.out.println(i + " value from JSONArray : " + jsonArrayObject.get(i));
            System.out.println("Data type of "+i+" Value in JsonArray is : "+jsonArrayObject.get(i).getClass());
        }

        // Fetch only single String
        String sSportAnswerValue = at.getStringByExpression(sJsonFile, "$..sport..answer");
        System.out.println(sSportAnswerValue);
    }

    @Test
    public void asd(){
        String s = "ststberries";
        System.out.println("asdadsasdasdasd"+s.substring(2,5));
    }
}
