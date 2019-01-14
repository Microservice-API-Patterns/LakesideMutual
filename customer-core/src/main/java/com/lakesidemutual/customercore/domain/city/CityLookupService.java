package com.lakesidemutual.customercore.domain.city;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.microserviceapipatterns.domaindrivendesign.DomainService;

/**
 * This is a DDD Domain Service and is automatically injected thanks
 * to the @Component annotation. Note that we could also have used the @Service
 * annotation, which is an alias for @Component.
 */
@Component
public class CityLookupService implements DomainService {
	private final static String CSV_FILE = "cities_by_postalcode_switzerland.csv";
	private final static char CSV_SEPARATOR = ';';
	private final static String POSTAL_CODE_KEY = "postalCode";
	private final static String CITY_KEY = "city";
	private static Multimap<String, String> lookupMap = null;
	private final static Logger logger = LoggerFactory.getLogger(CityLookupService.class);

	private static Multimap<String, String> loadLookupMap() {
		Multimap<String, String> map = TreeMultimap.create();
		try {
			InputStream file = new ClassPathResource(CSV_FILE).getInputStream();
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(CSV_SEPARATOR);
			MappingIterator<Map<String, String>> readValues = mapper.readerFor(Map.class).with(schema).readValues(file);
			List<Map<String, String>> values = readValues.readAll();

			for (Map<String, String> value : values) {
				String postalCode = value.get(POSTAL_CODE_KEY).trim();
				String city = value.get(CITY_KEY).trim();
				if (city == null || postalCode == null) {
					continue;
				}
				map.put(postalCode, city);
			}
		} catch (IOException e) {
			logger.error("Failed to create city lookup-map.", e);
		}
		return map;
	}

	private static Multimap<String, String> getLookupMap() {
		if (lookupMap == null) {
			lookupMap = loadLookupMap();
			logger.info("Loaded " + lookupMap.size() + " postal-code / city pairs.");
		}
		return lookupMap;
	}

	/**
	 * Returns an alphabetically ordered list of cities that match the given postal
	 * code.
	 */
	public List<String> getCitiesForPostalCode(String postalCode) {
		Multimap<String, String> lookupMap = getLookupMap();
		return new ArrayList<>(lookupMap.get(postalCode));
	}
}
