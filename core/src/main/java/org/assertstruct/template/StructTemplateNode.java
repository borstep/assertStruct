package org.assertstruct.template;

import org.assertstruct.impl.opt.NodeOptions;
import org.assertstruct.impl.opt.SubtreeOptions;
import org.assertstruct.impl.parser.ExtToken;

public interface StructTemplateNode extends TemplateNode {
    ExtToken getStartToken();


    NodeOptions getConfig();

    default void print(StringBuilder out, boolean forceComa, boolean forceEOL, int indent, boolean fromNewLine) {
        out.append(getStartToken().get_source(), getStartToken().getPrefix(), getEndToken().getPrefix() - getStartToken().getPrefix());
        getToken().print(out, forceComa, forceEOL);
    }


    void sealConfigs(SubtreeOptions parentConfig);

    TemplateNode getByKey(Object key);

    default boolean isDataNode() {
        return true;
    }
}
