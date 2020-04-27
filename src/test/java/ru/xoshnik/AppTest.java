package ru.xoshnik;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.siebel.data.SiebelPropertySet;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import org.junit.Test;
import org.slf4j.Logger;
import ru.xoshnik.converter.Converter;
import ru.xoshnik.converter.implementation.JsonToPropSetConverter;
import ru.xoshnik.converter.implementation.PropSetToJsonConverter;

public class AppTest
{

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AppTest.class);

    private final static Converter jsonToPropSetConverter = new JsonToPropSetConverter();

    private final static Converter propSetToJsonConverter = new PropSetToJsonConverter();

    private final static EAIJSONConverter converter = new EAIJSONConverter(
        jsonToPropSetConverter,
        propSetToJsonConverter
    );


    @Test
    public void glossary() {
        testJSON("json/glossary.json");
    }

    @Test
    public void menu() {
        testJSON("json/menu.json");
    }

    @Test
    public void widget() {
        testJSON("json/widget.json");
    }

    @Test
    public void webApp() {
        testJSON("json/web-app.json");
    }

    private void testJSON(String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject origJSON = getJsonFromResourceAsPS(fileName, gson);

        SiebelPropertySet siebelPropertySet = invokeMethod(
            getPropSetWithJson(origJSON),
            "JSONToPropSet"
        );

        SiebelPropertySet output = invokeMethod(siebelPropertySet, "PropSetToJSON");
        JsonObject newJSON = gson
            .fromJson(output.getProperty("JSON"), JsonObject.class);

        System.out.println(gson.toJson(origJSON));
        System.out.println(siebelPropertySet);
        System.out.println(gson.toJson(newJSON));

        assert (gson.toJson(origJSON).length() == gson.toJson(newJSON).length());
    }

    private SiebelPropertySet invokeMethod(SiebelPropertySet input,
        String methodName) {
        SiebelPropertySet output = new SiebelPropertySet();
        converter.doInvokeMethod(methodName, input, output);
        return output;
    }

    private SiebelPropertySet getPropSetWithJson(JsonObject jsonObject) {
        SiebelPropertySet siebelPropertySet = new SiebelPropertySet();
        siebelPropertySet.setProperty("JSON", jsonObject.toString());
        return siebelPropertySet;
    }

    private JsonObject getJsonFromResourceAsPS(String filePath, Gson gson) {
        JsonObject jsonObject = null;
        try {
            FileReader fileInputStream = null;
            URL resource = this.getClass().getClassLoader().getResource(filePath);
            if (resource != null) {
                fileInputStream = new FileReader(resource.getFile());
            }
            if (fileInputStream != null) {
                jsonObject = gson.fromJson(
                    fileInputStream,
                    JsonObject.class
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
