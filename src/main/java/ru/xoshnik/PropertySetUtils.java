package ru.xoshnik;

import com.siebel.data.SiebelPropertySet;

public class PropertySetUtils {

	public static SiebelPropertySet getChildByName(SiebelPropertySet ps, String name) {
		for (int i = 0; i < ps.getChildCount(); i++) {
			SiebelPropertySet child = ps.getChild(i);
			if (child.getType().equals(name)) {
				return child;
			}
		}
		return null;
	}

	public static void createOrUpdateChildByName(SiebelPropertySet ps, String name, String value) {
		for (int i = 0; i < ps.getChildCount(); i++) {
			SiebelPropertySet child = ps.getChild(i);
			if (child.getType().equals(name)) {
				child.setValue(value);
				return;
			}
		}
		SiebelPropertySet newChild = new SiebelPropertySet();
		newChild.setType(name);
		newChild.setValue(value);
		ps.addChild(newChild);
	}

}
