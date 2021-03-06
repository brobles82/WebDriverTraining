package com.octopus.utils;

import java.util.List;
import java.util.Optional;

/**
 * Defines a service for working with system properties
 */
public interface SystemPropertyUtils {

	/**
	 *
	 * @return A list of all the system properties with web start prefixes removed
	 */
	List<String> getNormalisedProperties();

	/**
	 * Extracts system properties, either from their default name, or with the javaws prefix
	 *
	 * @param name The name of the system property
	 * @return The value of the system property
	 */
	String getProperty(String name);

	/**
	 * Checks for the given system property
	 *
	 * @param name The name of the system property
	 * @return true if the property is defined, false otherwise
	 */
	boolean hasProperty(String name);

	/**
	 * Gets a system property as a boolean
	 * @param name The name of the property
	 * @param defaultValue The default value if the property is empty or null
	 * @return the boolean value of the system property, or the default value if the system property is empty
	 */
	boolean getPropertyAsBoolean(String name, boolean defaultValue);

	/**
	 * Gets a system property as a float
	 * @param name The name of the property
	 * @param defaultValue The default value if the property is empty or null
	 * @return the boolean value of the system property, or the default value if the system property is empty
	 */
	float getPropertyAsFloat(String name, float defaultValue);

	/**
	 * Gets a system property as a int
	 * @param name The name of the property
	 * @param defaultValue The default value if the property is empty or null
	 * @return the boolean value of the system property, or the default value if the system property is empty
	 */
	int getPropertyAsInt(String name, int defaultValue);

	/**
	 * Gets a system property as a long
	 * @param name The name of the property
	 * @param defaultValue The default value if the property is empty or null
	 * @return the boolean value of the system property, or the default value if the system property is empty
	 */
	long getPropertyAsLong(String name, long defaultValue);

	/**
	 * Extracts system properties, either from their default name, or with the javaws prefix.
	 * Treats empty strings as null.
	 *
	 * @param name The name of the system property
	 * @return The value of the system property
	 */
	String getPropertyEmptyAsNull(String name);

	/**
	 * Extracts system properties, either from their default name, or with the javaws prefix.
	 * Treats null strings as empty.
	 *
	 * @param name The name of the system property
	 * @return The value of the system property
	 */
	String getPropertyNullAsEmpty(String name);

	/**
	 * Extracts system properties, either from their default name, or with the javaws prefix.
	 * Treats null strings as empty. Appends a string to any non-empty properties.
	 *
	 * @param name The name of the system property
	 * @param append A string to append to a non-empty property
	 * @return The value of the system property
	 */
	String getPropertyNullAsEmpty(String name, String append);

	/**
	 * Extracts system properties, either from their default name, or with the javaws prefix.
	 * Treats empty strings as empty optional.
	 *
	 * @param name The name of the system property
	 * @return The value of the system property
	 */
	Optional<String> getPropertyAsOptional(String name);

	/**
	 * Return the command line arguments that are used to define the list of system properties
	 * @param propertyNames The system properties that are to be included in the command line args
	 * @return The command line args to be passed to the java executable
	 */
	String getPropertiesAsCommandLineRags(List<String> propertyNames);
}
