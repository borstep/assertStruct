package ua.kiev.its.assertstruct.result;

import ua.kiev.its.assertstruct.template.TemplateNode;

public interface ErrorResult<T extends TemplateNode> extends MatchResult<T> {

    @Override
    default boolean hasDifference() {
        return true;
    }

}
