package ru.xoshnik.converter;

import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;

public interface Converter {

	void process(SiebelPropertySet input, SiebelPropertySet output) throws SiebelBusinessServiceException;

}
