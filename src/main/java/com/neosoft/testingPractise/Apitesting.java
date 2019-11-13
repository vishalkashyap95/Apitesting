package com.neosoft.testingPractise;

import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Apitesting {

    public InputStream readProperty(String propFileName) {
        return getClass().getClassLoader().getResourceAsStream(propFileName);
    }

    // Create a connection with any URL and return it
    public HttpURLConnection openUrlConnection(String sUrl) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl);
            connection = (HttpURLConnection) url.openConnection();
            if (connection != null) {
                connection.setReadTimeout(15000);
                System.out.println("Successfully Established connection with URL : " + sUrl);
                System.out.println("Response Code : " + connection.getResponseCode());
                return connection;
            } else {
                System.out.println("FAILED to Establish connection.");
                return connection;
            }
        } catch (Exception e) {
            System.out.println("Exception caught in openUrlConnection() method : " + e.getMessage());
            e.printStackTrace();
            return connection;
        }
    }

    // Create a connection with URL(passed as a parameter) and get the response body and return it as String
    public String openUrlConAndGetContent(String sUrl) {
        HttpURLConnection con = null;
        StringBuilder sBuilder = null;
        BufferedReader reader = null;
        String line = null;
        try {
            con = this.openUrlConnection(sUrl);
            if (con != null) {
                sBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                if (reader != null && sBuilder != null) {
                    System.out.println("Reading content form URL...");
                    con.setReadTimeout(15000);
                    while ((line = reader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }
                    reader.close();
                    System.out.println("Successfully read the content from URL.");
                    return sBuilder.toString();
                } else {
                    System.out.println("FAILED to read content of URL.");
                    return sBuilder.toString();
                }
            } else {
                return sBuilder.toString();
            }
        } catch (Exception e) {
            System.out.println("Exception caught in openUrlConAndGetContent() method : " + e.getMessage());
            e.printStackTrace();
            return sBuilder.toString();
        }
    }


    // Read File and store it into String file and return it
    public String readFile(String fileName) {
        StringBuilder sb = null;
        BufferedReader reader = null;
        String line = null;
        try {
            InputStream fileInput = getClass().getClassLoader().getResourceAsStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fileInput));
            sb = new StringBuilder();
            if (reader != null && sb != null) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("Exception caught in method readFile() : " + e.getMessage());
            e.printStackTrace();
            return sb.toString();
        }
    }

    // This method will extract object with key value pair and return List of Map objects
    public List<Map<String, Object>> getKeyValueObjectByExpression(String sJson, String sExpression) {
        try {
            System.out.println(JsonPath.parse(sJson).read(sExpression).toString());
            System.out.println(JsonPath.parse(sJson).read(sExpression).toString().contains("Huston Rocket"));
            System.out.println(JsonPath.parse(sJson).read(sExpression).toString().contains("Which one is correct team name in NBA?"));
            return JsonPath.parse(sJson).read(sExpression);
        } catch (Exception e) {
            System.out.println("Exception caught in ");
            e.printStackTrace();
            return null;
        }
    }

    // This method will return list of objects based on expression
    public List<Object> getListOfObjectsByExpression(String sJson, String sExpression) {
        try {
            return JsonPath.parse(sJson).read(sExpression);
        } catch (Exception e) {
            System.out.println("Exception caught in getListOfObjectsByExpression() method");
            e.printStackTrace();
            return null;
        }
    }

    // This method will return String based on expression
    public String getStringByExpression(String sJson, String sExpression) {
        try {
            return JsonPath.parse(sJson).read(sExpression).toString();
        } catch (Exception e) {
            System.out.println("Exception caught in getObjectByExpression() method.");
            e.printStackTrace();
            return "";
        }
    }
}
