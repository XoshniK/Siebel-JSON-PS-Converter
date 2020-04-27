package ru.xoshnik;

import com.siebel.data.SiebelPropertySet;
import org.slf4j.Logger;
import ru.xoshnik.converter.Converter;
import ru.xoshnik.converter.implementation.JsonToPropSetConverter;
import ru.xoshnik.converter.implementation.PropSetToJsonConverter;


public class EAIJSONConverter extends com.siebel.eai.SiebelBusinessService {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(EAIJSONConverter.class);

	private final Converter jsonToPropSetConverter;

	private final Converter propSetToJsonConverter;

	public EAIJSONConverter() {
		this(new JsonToPropSetConverter(), new PropSetToJsonConverter());
	}

	public EAIJSONConverter(Converter jsonToPropSetConverter, Converter propSetToJsonConverter) {
		this.jsonToPropSetConverter = jsonToPropSetConverter;
		this.propSetToJsonConverter = propSetToJsonConverter;
	}

	@Override
	public void doInvokeMethod(String methodName, SiebelPropertySet input, SiebelPropertySet output) {
		if (methodName != null && input != null && output != null) {
			try {
				if (methodName.equals("PropSetToJSON")) {
					propSetToJsonConverter.process(input, output);
				}

				if (methodName.equals("JSONToPropSet")) {
					jsonToPropSetConverter.process(input, output);
				}
				setException(output, "0", "");
			} catch (Exception ex) {
				log.error(ex.getLocalizedMessage(), ex);
				setException(output, "1", ex.getLocalizedMessage());
			}
		}
	}

	private void setException(SiebelPropertySet ps, String code, String message) {
		PropertySetUtils.createOrUpdateChildByName(ps, "ErrorCode", code);
		PropertySetUtils.createOrUpdateChildByName(ps, "ErrorMessage", message);
	}

}
