package org.assertstruct;

import lombok.Getter;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.ValueWrapper;

@Getter
public class AssertionStructFailedError extends AssertionFailedError {
    private static final long serialVersionUID = 1L;
    private final ValueWrapper actualSource;

    public AssertionStructFailedError(String message, String expectedStr, String actualStr, Object actual) {
        super(message, expectedStr, actualStr);
        this.actualSource = ValueWrapper.create(actual);
    }
}
