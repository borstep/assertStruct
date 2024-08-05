package ua.kiev.its.assertstruct.config;

public interface JsonConverterI {

    /**
     * Convert POJO to JSON compatible structure
     *
     * @param value
     * @return
     */
    Object pojo2json(Object value);
}
