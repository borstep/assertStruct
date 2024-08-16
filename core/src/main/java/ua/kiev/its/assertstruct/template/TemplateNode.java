package ua.kiev.its.assertstruct.template;

import ua.kiev.its.assertstruct.service.SharedValidator;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.impl.validator.TypeCheckValidator;
import ua.kiev.its.assertstruct.result.MatchResult;
import ua.kiev.its.assertstruct.template.node.ArrayNode;
import ua.kiev.its.assertstruct.template.node.ObjectNode;

import java.io.IOException;
import java.util.Set;

public interface TemplateNode extends MatchResult<TemplateNode> {


//    void setToken(ExtToken token);

    ExtToken getToken();

    TemplateKey getKey();

    default boolean isScalar() {
        return true;
    }

    default boolean isStruct() {
        return !isScalar();
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

}
