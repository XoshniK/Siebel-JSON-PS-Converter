package ru.xoshnik.converter.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.siebel.data.SiebelPropertySet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import ru.xoshnik.PropertySetUtils;
import ru.xoshnik.converter.Converter;
import ru.xoshnik.converter.PsComponent;

public class PropSetToJsonConverter implements Converter {

	@Override
	public void process(SiebelPropertySet input, SiebelPropertySet output) {
		SiebelPropertySet child = PropertySetUtils.getChildByName(input, "SiebelMessage");
		if (child != null) {
			JsonObject myJSON = traversePS(child);
			output.setProperty("JSON", myJSON.toString());
		}
	}


	private static JsonObject traversePS(SiebelPropertySet ps) {
		JsonObject siebJSON = new JsonObject();

		String propVal;
		for (String propName = ps.getFirstProperty(); !propName.equals(""); propName = ps.getNextProperty()) {
			propVal = ps.getProperty(propName);
			siebJSON.addProperty(propName, propVal);
		}
		for (int i = 0; i < ps.getChildCount(); ++i) {
			if (ps.getChild(i).getType().endsWith("_set")) {
				ps.getChild(i).setType(ps.getChild(i).getType().substring(0, ps.getChild(i).getType().length() - 4));
			}
			JsonArray ja;
			if (ps.getChild(i).getType().startsWith(PsComponent.LIST_OF + PsComponent.OBJECT_LIST)) {
				ja = new JsonArray();
				for (int o = 0; o < ps.getChild(i).getChildCount(); ++o) {
					ja.add(traversePS(ps.getChild(i).getChild(o)));
				}
				siebJSON.add(ps.getChild(i).getType().substring((PsComponent.LIST_OF + PsComponent.OBJECT_LIST).length()), ja);
			} else if (!ps.getChild(i).getType().startsWith(PsComponent.PRIMITIVE_LIST)) {
				if (ps.getChild(i).getType().startsWith(PsComponent.LIST_OF) && !ps.getChild(i).getType()
						.startsWith(PsComponent.LIST_OF + PsComponent.OBJECT_LIST)) {
					siebJSON.add(
							ps.getChild(i).getType().substring(PsComponent.LIST_OF.length()),
							traversePS(ps.getChild(i).getChild(0))
					);
				} else {
					siebJSON.add(ps.getChild(i).getType(), traversePS(ps.getChild(i)));
				}
			} else {
				ja = new JsonArray();
				SiebelPropertySet auxps = ps.getChild(i);
				Enumeration e = auxps.getPropertyNames();
				List list = Collections.list(e);
				Collections.sort(list);
				e = Collections.enumeration(list);
				while (e.hasMoreElements()) {
					String propName1 = (String) e.nextElement();
					propVal = auxps.getProperty(propName1);
					ja.add(new JsonPrimitive(propVal));
				}
				siebJSON.add(auxps.getType().substring(PsComponent.PRIMITIVE_LIST.length()), ja);
			}
		}

		return siebJSON;
	}

}
