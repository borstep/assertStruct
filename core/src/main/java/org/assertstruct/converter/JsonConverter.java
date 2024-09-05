package org.assertstruct.converter;

import org.assertstruct.service.exceptions.MatchingFailure;

public interface JsonConverter {

    /**
     * Convert POJO to JSON compatible structure
     *
     * @param value POJO
     * @return JSON compatible structure
     */
    Object pojo2json(Object value) throws MatchingFailure;

    /**
     * Convert value to targetClass
     * @param value source
     * @param toValueType target class
     * @return converted value
     */
    <T> T convert(Object value, Class<T> toValueType);
}
