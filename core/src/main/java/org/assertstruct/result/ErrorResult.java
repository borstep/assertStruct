package org.assertstruct.result;

public interface ErrorResult extends MatchResult {

    @Override
    default boolean hasDifference() {
        return true;
    }

}
