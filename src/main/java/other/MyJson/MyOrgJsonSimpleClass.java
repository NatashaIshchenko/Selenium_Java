package other.MyJson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


/**
 * класс содержащий функции для работы с JSON c использованием библиотеки org.json.simple
 */
public class MyOrgJsonSimpleClass {

    /**
     * основная функция
     */
    public static void main(String [ ] args) throws Exception {
        MyOrgJsonSimpleClass testClass = new MyOrgJsonSimpleClass();
        testClass.createAndGetJsonObject();
        testClass.createAndGetJsonArray();
        testClass.funcSimpleJSONParser();
    }

    /**
     * конструктор класса
     */
    private MyOrgJsonSimpleClass(){
        //тело
    }

    /**
     * способы создания org.json.simple.JSONObject
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

        //get(String key) – gets the object associated with the supplied key
        Object ob =  jo1.get("age");
        System.out.println("jo1.get(\"age\") = "+ob.toString());
    }

    /**
     * способы создания org.json.simple.JSONArray
     */
    private void createAndGetJsonArray() {
        //Creating JSONArray
        JSONArray ja1 = new JSONArray();
        ja1.add(Boolean.TRUE);
        ja1.add("lorem ipsum");
        JSONObject jo1 = new JSONObject();
        jo1.put("name", "jon doe");
        jo1.put("age", "22");
        jo1.put("city", "chicago");
        ja1.add(jo1);
        System.out.println("Creating JSONArray: "+ja1);

        //get(index) – gets the object by index
        Object ob =  ja1.get(1);
        System.out.println("ja1.get(1) = "+ob.toString());
    }

    /**
     * org.json.simple.parser.JSONParser
     */
    private void funcSimpleJSONParser() throws Exception{
        JSONParser parser = new JSONParser();

        //parse to org.json.simple.JsonObject
        String filePath1 = "src/main/resources/JsonObjectExample1.json";
        Object obj1 = parser.parse(new FileReader(filePath1));
        JSONObject jsonObject1 = (JSONObject) obj1;
        System.out.println("funcSimpleJSONParser - for JsonObject: " + jsonObject1.toString());

        //parse to org.json.simple.JsonArray
        String filePath2 = "src/main/resources/JsonArrayExample1.json";
        Object obj2 = parser.parse(new FileReader(filePath2));
        JSONArray jsonObject2 = (JSONArray) obj2;
        System.out.println("funcSimpleJSONParser - for JsonArray: " + jsonObject2.toString());
    }
}