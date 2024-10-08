package org.assertstruct.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.assertstruct.template.TemplateNode;

@Value
@AllArgsConstructor
public class ErrorValue implements ErrorResult {
    Object source;
    @Getter
    TemplateNode matchedTo;

    public ErrorValue(Object source) {
        this(source, null);
    }

}
