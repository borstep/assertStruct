package org.assertstruct.converter;

import org.assertstruct.service.exceptions.MatchingFailure;

public interface JsonConverter {

    /**
     * Convert POJO to JSON compatible structure
     *
     * @param value
     * @return
     */
    Object pojo2json(Object value) throws MatchingFailure;

    <T> T convert(Object value, Class<T> toValueType);
}
