package org.assertstruct.result;

import org.assertstruct.template.TemplateNode;

public interface MatchResult<T extends TemplateNode> {
    boolean hasDifference();

    TemplateNode getMatchedTo();

    default boolean isConfig() {
        return false;
    }


/*
    default String asString() {
        try {
            StringBuilder builder = new StringBuilder();
            print(builder);
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
*/
}
