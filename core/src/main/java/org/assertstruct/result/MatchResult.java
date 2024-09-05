package org.assertstruct.result;

import org.assertstruct.template.TemplateNode;

public interface MatchResult {
    boolean hasDifference();

    TemplateNode getMatchedTo();

    default boolean isConfig() {
        return false;
    }

}
