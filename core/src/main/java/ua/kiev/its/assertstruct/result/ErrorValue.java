package ua.kiev.its.assertstruct.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import ua.kiev.its.assertstruct.template.TemplateNode;

@Value
@AllArgsConstructor
public class ErrorValue implements ErrorResult<TemplateNode> {
    Object source;
    @Getter
    TemplateNode matchedTo;

    public ErrorValue(Object source) {
        this(source, null);
    }

}
