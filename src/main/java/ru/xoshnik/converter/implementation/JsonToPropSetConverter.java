package ru.xoshnik.converter.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.siebel.data.SiebelPropertySet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import ru.xoshnik.PropertySetUtils;
import ru.xoshnik.converter.Converter;
import ru.xoshnik.converter.PsComponent;

public class JsonToPropSetConverter implements Converter {

	@Override
	public void process(SiebelPropertySet input, SiebelPropertySet output) {
		String json = input.getProperty("JSON");
		if (json != null) {
			JsonObject obj = new Gson().fromJson(json, JsonObject.class);
			SiebelPropertySet siebelMessage = JsonObjectToPropertySet(obj, new SiebelPropertySet());
			siebelMessage.setType("SiebelMessage");
			output.addChild(siebelMessage);
		}
	}

	private static SiebelPropertySet JsonObjectToPropertySet(JsonObject jsonObj, SiebelPropertySet ps) {
		Iterator<Entry<String, JsonElement>> iterator = jsonObj.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mapEntry = iterator.next();
			if (mapEntry != null) {
				JsonElement jsonelement = (JsonElement) mapEntry.getValue();
				SiebelPropertySet child;
				if (jsonelement.isJsonArray()) {
					JsonArray jsonArray = jsonelement.getAsJsonArray();
					child = new SiebelPropertySet();
					if (jsonArray.size() > 0 && jsonArray.get(0).isJsonPrimitive()) {
						child.setType(PsComponent.PRIMITIVE_LIST + mapEntry.getKey().toString());
					} else {
						child.setType(PsComponent.LIST_OF + PsComponent.OBJECT_LIST + mapEntry.getKey().toString());
					}

					for (int i = 0; i < jsonArray.size(); ++i) {
						if (jsonArray.get(i).isJsonPrimitive()) {
							child.setProperty(String.valueOf(i), jsonArray.get(i).getAsString());
						} else {
							SiebelPropertySet temp = new SiebelPropertySet();
							temp.setType(PsComponent.OBJECT_LIST + mapEntry.getKey().toString());
							if (jsonArray.get(i).isJsonObject()) {
								child.addChild(JsonObjectToPropertySet(jsonArray.get(i).getAsJsonObject(), temp));
							} else {
								JsonObject aux = new JsonObject();
								aux.add(String.valueOf(i), jsonArray.get(i));
								child.addChild(JsonObjectToPropertySet(aux, temp));
							}
						}
					}
					ps.addChild(child);
				} else if (jsonelement.isJsonObject()) {
					JsonObject jsonObject = jsonelement.getAsJsonObject();
					child = new SiebelPropertySet();
					child.setType(mapEntry.getKey().toString());
					ps.addChild(JsonObjectToPropertySet(jsonObject, child));
				} else {
					ps.setProperty(mapEntry.getKey().toString(), removeQuotes(mapEntry.getValue().toString()));
				}
			}
		}

		return ps;
	}

	private static String removeQuotes(String inputString) {
		if (inputString.charAt(0) == '\"' && inputString.charAt(inputString.length() - 1) == '\"'
				|| inputString.charAt(0) == '\'' && inputString.charAt(inputString.length() - 1) == '\'') {
			return inputString.substring(1, inputString.length() - 1);
		}
		return inputString;
	}

}
