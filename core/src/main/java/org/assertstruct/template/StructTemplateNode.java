package org.assertstruct.template;

import org.assertstruct.impl.parser.ExtToken;

public interface StructTemplateNode extends StructTemplateShared, TemplateNode {
    ExtToken getStartToken();

    default void print(StringBuilder out, boolean forceComa, boolean forceEOL, int indent, boolean fromNewLine) {
        out.append(getStartToken().get_source(), getStartToken().getPrefix(), getEndToken().getPrefix() - getStartToken().getPrefix());
        getToken().print(out, forceComa, forceEOL);
    }


    TemplateNode getByKey(Object key);

    default boolean isDataNode() {
        return true;
    }
}
