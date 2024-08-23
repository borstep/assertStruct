package org.assertstruct.result;

import lombok.Value;
import lombok.experimental.Delegate;

@Value
public class RootResult implements MatchResult {
    @Delegate
    MatchResult delegate;

    Object matchedValue;

}
