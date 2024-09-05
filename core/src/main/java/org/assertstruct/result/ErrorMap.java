package org.assertstruct.result;

import lombok.Getter;
import org.assertstruct.template.node.ObjectNode;

import java.util.LinkedHashMap;

@Getter
public class ErrorMap extends LinkedHashMap<Object, MatchResult> implements ErrorResult {
    private final ObjectNode matchedTo;

    public ErrorMap(ObjectNode matchedTo) {
        this.matchedTo = matchedTo;
    }

}
