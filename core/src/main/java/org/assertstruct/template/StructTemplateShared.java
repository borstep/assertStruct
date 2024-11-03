package org.assertstruct.template;

import org.assertstruct.impl.opt.NodeOptions;
import org.assertstruct.impl.opt.SubtreeOptions;
import org.assertstruct.impl.parser.ExtToken;

public interface StructTemplateShared {
    ExtToken getStartToken();

    NodeOptions getConfig();

    void sealConfigs(SubtreeOptions parentConfig);

    /**
     * Is content of container is in same line as start token, at least first child
     * @return
     */
    boolean isInline();

    /**
     * last child has trailing comma
     * @return
     */
    Boolean getTrailingComa();

    /**
     * default indent calculated from first child
     * @return
     */
    Integer getDefaultIndent();

    /**
     * first inline element indent calculated from first child
     * @return
     */
    Integer getFirstInlineElementIndent();

    /**
     * separator between inline elements
     * @return
     */
    Integer getInlineElementsSeparator();

}
