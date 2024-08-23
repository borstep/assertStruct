package org.assertstruct.result;

import org.assertstruct.template.TemplateNode;

public interface ErrorResult<T extends TemplateNode> extends MatchResult<T> {

    @Override
    default boolean hasDifference() {
        return true;
    }

}
