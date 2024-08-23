package org.assertstruct.result;

import lombok.Getter;
import org.assertstruct.template.node.ObjectNode;

import java.util.LinkedHashMap;

public class ErrorMap extends LinkedHashMap<Object, MatchResult> implements ErrorResult<ObjectNode> {
    @Getter
    private ObjectNode matchedTo;

    public ErrorMap(ObjectNode matchedTo) {
        this.matchedTo = matchedTo;
    }

}
