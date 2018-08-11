package other.MyJson;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.*;
import java.io.FileReader;


/**
 * класс содержащий функции для работы с JSON c использованием библиотеки org.json
 *
 * http://www.baeldung.com/java-org-json
 org.json.JSONObject – similar to Java’s native Map like object which stores unordered key-value pairs
 org.json.JSONArray – an ordered sequence of values similar to Java’s native Vector implementation
 org.json.CDL – a tool that provides methods to convert comma-delimited text into a JSONArray and vice versa
 org.json.JSONTokener – This class parses a JSON string and is also used internally by the JSONObject and JSONArray classes to parse JSON Strings
 org.json.Cookie – converts from JSON String to cookies and vice versa
 org.json.HTTP – used to convert from JSON String to HTTP headers and vice versa
 org.json.JSONException – this is a standard exception thrown by this library
 org.json.JSONWriter – This class represents a method to produce JSON text. It has an append(String) method to append a string to a JSON text, key(String) and value(String) method to add key and values to JSON string. It can also write an array.
 */
public class MyOrgJsonClass {

    /**
     * основная функция
     */
    public static void main(String [ ] args) throws Exception {
        MyOrgJsonClass testClass = new MyOrgJsonClass();
        testClass.createAndGetJsonObject();
        testClass.createJsonArray();
        testClass.funcWithCDL();
        testClass.funcJSONTokener();
    }

    /**
     * конструктор класса
     */
    private MyOrgJsonClass(){
        //тело
    }

    /**
     * способы создания org.json.JsonObject
     */
    private void createAndGetJsonObject(){

        //Creating JSON Directly from JSONObject
        JSONObject jo1 = new JSONObject();
        jo1.put("name", "jon doe");
        jo1.put("age", "22");
        jo1.put("city", "chicago");
        System.out.println("Creating JSON Directly from JSONObject: " + jo1);

        //Creating JSON from Map
        Map<String, String> map = new HashMap<>();
        map.put("name", "jon doe");
        map.put("age", "22");
        map.put("city", "chicago");
        JSONObject jo2 = new JSONObject(map);
        System.out.println("Creating JSON from Map: "+jo2);

        //Creating JSONObject from JSON String
        JSONObject jo3 = new JSONObject(
                "{\"city\":\"chicago\",\"name\":\"jon doe\",\"age\":\"22\"}"
        );
        System.out.println("Creating JSONObject from JSON String: "+jo3);

        //get(String key) – gets the object associated with the supplied key, throws JSONException if the key is not found
        Object ob =  jo1.get("age");
        System.out.println("jo1.get(\"age\") = "+ob.toString());

        //opt(String key)- gets the object associated with the supplied key, null otherwise
        System.out.println("jo1.opt(\"NOTage\") = "+jo1.opt("NOTage"));
    }

    /**
     * способы создания org.json.JSONArray
     */
    private void createJsonArray(){

        //Creating JSONArray
        JSONArray ja1 = new JSONArray();
        ja1.put(Boolean.TRUE);
        ja1.put("lorem ipsum");
        JSONObject jo1 = new JSONObject();
        jo1.put("name", "jon doe");
        jo1.put("age", "22");
        jo1.put("city", "chicago");
        ja1.put(jo1);
        System.out.println("Creating JSONArray: "+ja1);

        //Creating JSONArray Directly from JSON String
        JSONArray ja2 = new JSONArray("[true, \"lorem ipsum\", 215]");
        System.out.println("Creating JSONArray Directly from JSON String: "+ja2);

        //Creating JSONArray Directly from a Collection or an Array
        List<String> list = new ArrayList<>();
        list.add("California");
        list.add("Texas");
        list.add("Hawaii");
        list.add("Alaska");
        JSONArray ja3 = new JSONArray(list);
        System.out.println("Creating JSONArray Directly from a Collection or an Array: "+ja3);
    }

    /**
     * использование org.json.CDL
     */
    private void funcWithCDL(){

        //Producing JSONArray of JSONObjects Using Comma Delimited Text (Case1)
        String string1 = "name, city, age \n" +
                "john, chicago, 22 \n" +
                "gary, florida, 35 \n" +
                "sal, vegas, 18";
        JSONArray result1 = CDL.toJSONArray(string1);
        System.out.println("Producing JSONArray of JSONObjects Using Comma Delimited Text(Case1): "+result1);

        //Producing JSONArray of JSONObjects Using Comma Delimited Text (Case2)
        JSONArray ja = new JSONArray();
        ja.put("name");
        ja.put("city");
        ja.put("age");
        String string2 = "john, chicago, 22 \n"
                + "gary, florida, 35 \n"
                + "sal, vegas, 18";
        JSONArray result2 = CDL.toJSONArray(ja, string2);
        System.out.println("Producing JSONArray of JSONObjects Using Comma Delimited Text(Case2): "+result2);

        //From JSONArray to String using CDL
        JSONArray ja2 = new JSONArray("[\"England\",\"USA\",\"Canada\"]");
        String cdt = CDL.rowToString(ja2);
        System.out.println("From JSONArray to String using CDL: "+cdt);
    }

    /**
     * org.json.JSONTokener – This class parses a JSON string and is also used internally by the JSONObject and JSONArray classes to parse JSON Strings
     */
    private void funcJSONTokener() throws Exception{
        // parsing file "JsonObjectExample1.json" using org.json.JSONTokener
        String filePath1 = "src/main/resources/JsonObjectExample1.json";
        JSONTokener jstok1 = new JSONTokener(new FileReader(filePath1));
        JSONObject jo1 = new JSONObject(jstok1);
        System.out.println("JSONObject(JSONTokener(JSON)): " + jo1.toString());

        // parsing file "JsonArrayExample1.json" using org.json.JSONTokener
        String filePath2 = "src/main/resources/JsonArrayExample1.json";
        JSONTokener jstok2 = new JSONTokener(new FileReader(filePath2));
        JSONArray jo2 = new JSONArray(jstok2);
        System.out.println("JSONObject(JSONTokener(JSON)): " + jo2.toString());
    }
}