package org.assertstruct.template;

import org.assertstruct.impl.factories.array.RepeaterNode;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.impl.validator.TypeCheckValidator;
import org.assertstruct.result.MatchResult;
import org.assertstruct.service.SharedValidator;
import org.assertstruct.template.node.ArrayNode;
import org.assertstruct.template.node.ObjectNode;

import java.io.IOException;
import java.util.Set;

public interface TemplateNode extends MatchResult {

    ExtToken getToken();

    TemplateKey getKey();

    default boolean isScalar() {
        return !isStruct();
    }

    default boolean isStruct() {
        return isArray() || isObject();
    }

    default boolean isArray() {
        return false;
    }

    default boolean isObject() {
        return false;
    }

    default void printDebug(StringBuilder out) throws IOException {
        ExtToken token = getToken();
        if (token != null) {
            token.print(out, false, false);
        }
    }

    default void print(StringBuilder out, boolean forceComa, boolean forceEOL, int indent, boolean fromNewLine) {
        ExtToken token = getToken();
        token.print(out, forceComa, forceEOL);
    }

    default ExtToken getStartToken() {
        return getToken();
    }

    default ExtToken getEndToken() {
        return getToken();
    }

    @Override
    default boolean hasDifference() {
        return false;
    }

    default void printStart(StringBuilder out) {
        if (isScalar()) {
            getStartToken().printPrefix(out);
        } else {
            getStartToken().print(out, false, false);
        }
    }

    default void printEnd(StringBuilder out, boolean forceComa, boolean forceEOL) {
        if (isScalar()) {
            getToken().printSuffix(out, forceComa, forceEOL);
        } else {
            getToken().print(out, forceComa, forceEOL);
        }
    }

    @Override
    default TemplateNode getMatchedTo() {
        return this;
    }

    default ObjectNode asDict() {
        return (ObjectNode) this;
    }

    default ArrayNode asArray() {
        return (ArrayNode) this;
    }

    Set<SharedValidator> getValidators();

    void addSharedValidator(TypeCheckValidator typeValidator);

    default boolean isDataNode() {
        return false;
    }

    default boolean isRepeater() {
        return this instanceof RepeaterNode;
    }

    default boolean isRepeaterFor(TemplateNode node) {
        return false;
    }

    default boolean isNotRepeater() {
        return !isRepeater();
    }


}
