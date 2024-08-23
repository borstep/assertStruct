package org.assertstruct.matcher;

import lombok.Value;

@Value
public class ValueWrapper {
    Object value;
    Object source;
}
