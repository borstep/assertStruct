package org.assertstruct.converter;

public interface JsonConverter {

    /**
     * Convert POJO to JSON compatible structure
     *
     * @param value
     * @return
     */
    Object pojo2json(Object value);
}
