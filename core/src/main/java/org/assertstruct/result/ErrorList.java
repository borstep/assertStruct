package org.assertstruct.result;

import lombok.Getter;
import org.assertstruct.template.node.ArrayNode;

import java.util.ArrayList;

@Getter
public class ErrorList extends ArrayList<MatchResult> implements ErrorResult {
    private final ArrayNode matchedTo;

    public ErrorList(ArrayNode matchedTo) {
        this.matchedTo = matchedTo;
    }

}
