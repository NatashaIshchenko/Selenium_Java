package other.MyJson;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * класс содержащий функции для работы с JSON c использованием библиотеки com.google.gson
 *
 * http://tutorials.jenkov.com/java-json/gson.html
 * http://www.javased.com/?api=com.google.gson.JsonParser
 * https://www.programcreek.com/java-api-examples/com.google.gson.JsonParser
*/
public class MyGsonClass {

    /**
     * основная функция
     */
    public static void main(String [] args) throws Exception {
        MyGsonClass testClass = new MyGsonClass();
        testClass.funcDeserialization();
        testClass.funcSerialization();
        testClass.funcJsonReader();
        testClass.funcJsonParser();
        testClass.funcForJson();
    }

    /**
     * конструктор класса
     */
    private MyGsonClass(){
        //тело
    }

    /**
     * вутренний класс
     */
    public class Car {
        public String brand = null;
        public int    doors = 0;
    }

    /**
     * Deserialization (JSON Into JavaObjects)
     */
    private void funcDeserialization(){
        Gson gson = new Gson();

        String json = "{\"brand\":\"Jeep\", \"doors\": 3}";
        Car car = gson.fromJson(json, Car.class);

        int one1 = gson.fromJson("1", int.class);
        Integer one2 = gson.fromJson("1", Integer.class);
        Long one3 = gson.fromJson("1", Long.class);
        Boolean bool1 = gson.fromJson("false", Boolean.class);
        String str = gson.fromJson("\"abc\"", String.class);
    }

    /**
     * Serialization (JavaObjects into JSON)
     */
    private void funcSerialization(){
        Gson gson = new Gson();

        Car car = new Car();
        car.brand = "Rover";
        car.doors = 5;
        String json = gson.toJson(car);
        System.out.println("func2: " + json);

        String zn1 = gson.toJson(1);
        String zn2 = gson.toJson("abcd");
        String zn3 = gson.toJson(10, Long.class);
        int[] values = {1};
        String zn4 = gson.toJson(values);
    }

    /**
     * com.google.gson.stream.JsonReader and com.google.gson.stream.JsonToken;
     */
    private void funcJsonReader() throws Exception{
        String json = "{\"brand\" : \"Toyota\", \"doors\" : 5}";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        try {
            while(jsonReader.hasNext()){
                JsonToken nextToken = jsonReader.peek();
                System.out.println("nextToken = " + nextToken);
                if(JsonToken.BEGIN_OBJECT.equals(nextToken)){
                    jsonReader.beginObject();
                } else if(JsonToken.NAME.equals(nextToken)){
                    String name  =  jsonReader.nextName();
                    System.out.println(name);
                } else if(JsonToken.STRING.equals(nextToken)){
                    String value =  jsonReader.nextString();
                    System.out.println(value);
                } else if(JsonToken.NUMBER.equals(nextToken)){
                    long value =  jsonReader.nextLong();
                    System.out.println(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * com.google.gson.JsonParser
     */
    private void  funcJsonParser() throws Exception{
        JsonParser parser = new JsonParser();

        //-------------Example #1
        String json = "{ \"f1\":\"Hello\",\"f2\":{\"f3:\":\"World\"}}";
        JsonElement jsonTree = parser.parse(json);
        JsonObject jsonObject = null;
        JsonArray jsonArray = null;
        JsonPrimitive jsonPrimitive = null;
        if(jsonTree.isJsonObject()){
            jsonObject = jsonTree.getAsJsonObject();
        } else if (jsonTree.isJsonArray()){
            jsonArray = jsonTree.getAsJsonArray();
        } else if (jsonTree.isJsonPrimitive()){
            jsonPrimitive = jsonTree.getAsJsonPrimitive();
        } else if (jsonTree.isJsonNull())
            System.out.println("jsonTree.isJsonNull() = true");

        //-------------Example #2
        String json1 = "{ \"f1\":\"Hello\",\"f2\":{\"f3:\":\"World\"}}";
        JsonElement jsonTree1 = parser.parse(json1);
        if(jsonTree1.isJsonObject()){
            JsonObject jsonObject1 = jsonTree1.getAsJsonObject();
            JsonElement f1 = jsonObject1.get("f1");
            JsonElement f2 = jsonObject1.get("f2");
            if(f2.isJsonObject()){
                JsonObject f2Obj = f2.getAsJsonObject();
                JsonElement f3 = f2Obj.get("f3");
            }
        }
        //-------------Example #3 with JsonObject
        String filePath1 = "src/main/resources/JsonObjectExample1.json";
        JsonElement jsonTree2 = parser.parse(new FileReader(filePath1));
        JsonObject jsonObject2 = jsonTree2.getAsJsonObject();
        System.out.println("JsonObject = " + jsonObject2);

        //-------------Example #4 with JsonArray
        String filePath2 = "src/main/resources/JsonArrayExample1.json";
        JsonElement jsonTree3 = parser.parse(new FileReader(filePath2));
        JsonArray jsonArray3 = jsonTree3.getAsJsonArray();
        System.out.println("JsonArray = " + jsonArray3);
    }


    /**
     * полезные функции для работы с json
     */
    private void funcForJson(){
        String json1 = "{ \"f1\":\"Hello\",\"f2\":{\"f3:\":\"World\"}}";
        String json2 = "[ \"f4\",\"f5\",\"f6\"]";
        JsonParser parser = new JsonParser();

        JsonObject jsonObject1 = parser.parse(json1).getAsJsonObject();
        System.out.println("jsonObject #1 = " + jsonObject1);
        jsonObject1.addProperty("property1", "value1");
        System.out.println("jsonObject #2 = " + jsonObject1);

        JsonArray jsonArray1 = parser.parse(json2).getAsJsonArray();
        System.out.println("jsonArray #1 = " + jsonArray1);
        jsonArray1.add("f7");
        System.out.println("jsonArray #2 = " + jsonArray1);

        jsonObject1.add("f9", jsonArray1);
        System.out.println("jsonObject #3 = " + jsonObject1);

        System.out.println("jsonObject1.get(\"f9\") = " + jsonObject1.get("f9").toString());
    }
}
