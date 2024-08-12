package ua.kiev.its.assertstruct.template;

import lombok.NonNull;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;

public abstract class AbstractEvaluatorKey extends TemplateKey implements EvaluatorTemplateKey {
    public AbstractEvaluatorKey(@NonNull String value, ExtToken token) {
        super(value, token);
    }

    @Override
    public TemplateKeyType getType() {
        return TemplateKeyType.EVALUATOR;
    }

}
