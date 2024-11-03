package data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

// Include Java class simple-name as JSON property "type"
@JsonTypeInfo(use = Id.SIMPLE_NAME, property = "type")
// Required for deserialization only
@JsonSubTypes({@Type(PolymorphString.class), @Type(PolymorphInt.class)})
public abstract class PolymorphBase {
}
