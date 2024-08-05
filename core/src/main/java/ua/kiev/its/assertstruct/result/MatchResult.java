package ua.kiev.its.assertstruct.result;

import ua.kiev.its.assertstruct.template.TemplateNode;

import java.io.IOException;

public interface MatchResult<T extends TemplateNode> {
    boolean hasDifference();

    T getMatchedTo();

    default String asString() {
        try {
            Json5Printer printer = new Json5Printer();
            printer.print(this);
            return printer.getOut().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
