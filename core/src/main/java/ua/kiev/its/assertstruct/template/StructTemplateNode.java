package ua.kiev.its.assertstruct.template;

import ua.kiev.its.assertstruct.impl.config.NodeConfig;
import ua.kiev.its.assertstruct.impl.config.SubtreeConfig;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;

import java.io.IOException;

public interface StructTemplateNode extends TemplateNode {
    ExtToken getStartToken();

    @Override
    default boolean isScalar() {
        return false;
    }

    NodeConfig getConfig();

    default void print(StringBuilder out, boolean forceComa, boolean forceEOL, int indent, boolean fromNewLine) throws IOException {
        out.append(getStartToken().get_source(), getStartToken().getPrefix(), getEndToken().getPrefix() - getStartToken().getPrefix());
        getToken().print(out, forceComa, forceEOL);
    }


    void sealConfigs(SubtreeConfig parentConfig);

    TemplateNode getByKey(Object key);

    default boolean isDataNode() {
        return true;
    }
}
