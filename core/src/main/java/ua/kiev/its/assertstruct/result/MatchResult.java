package ua.kiev.its.assertstruct.result;

import ua.kiev.its.assertstruct.template.TemplateNode;

import java.io.IOException;

public interface MatchResult<T extends TemplateNode> {
    boolean hasDifference();

    T getMatchedTo();

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
