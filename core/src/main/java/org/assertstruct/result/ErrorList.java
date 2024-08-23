package org.assertstruct.result;

import lombok.Getter;
import org.assertstruct.template.node.ArrayNode;

import java.util.ArrayList;

public class ErrorList extends ArrayList<MatchResult> implements ErrorResult<ArrayNode> {
    @Getter
    private ArrayNode matchedTo;

    public ErrorList(ArrayNode matchedTo) {
        this.matchedTo = matchedTo;
    }

}