package org.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import data.TestPojo;
import data.ValueObject;
import org.assertstruct.AssertStruct;
import org.assertstruct.converter.MapWrapper;
import org.junit.jupiter.api.Test;

import static data.TestPojo.*;
import static org.junit.jupiter.api.Assertions.*;

class JacksonConverterTest {
    JacksonConverter converter = new JacksonConverter(AssertStruct.getDefault().getConfig());


    @Test
    void mapTest() throws JsonProcessingException {
        TestPojo firstChild, firstElement;
        ValueObject valueObject;
        TestPojo pojo= pojo()
                .child("1", firstChild=nextPojo())
                .child("2", nextPojo())
                .element(firstElement=nextPojo())
                .element(nextPojo())
                .other("key1", "value")
                .other("key2", "value")
                .value(valueObject=new ValueObject("value"))
                .build();
        Object expected = converter.pojo2jsonBase(pojo);
        String expectedStr = converter.getBaseMapper().writeValueAsString(expected);
        MapWrapper actual = (MapWrapper) converter.pojo2json(pojo);
        String actualStr = converter.getBaseMapper().writeValueAsString(actual);
        assertEquals(expectedStr, actualStr);

        assertSame(pojo, actual.getSource(), "Root should be the pojo");
        assertSame(firstChild, actual.getChildWrapper("children").getChildSource("1"), "First child source should be correct");
        assertSame(firstElement, actual.getChildWrapper("elements").getChildSource(0), "First element source should be the correct");
        assertSame(valueObject, actual.getChildSource("value"), "Value object should be the correct");

    }
}