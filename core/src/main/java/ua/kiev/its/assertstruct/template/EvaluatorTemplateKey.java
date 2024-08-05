package ua.kiev.its.assertstruct.template;

import ua.kiev.its.assertstruct.matcher.Matcher;

public interface EvaluatorTemplateKey {
    Object evaluate(Object value, Matcher context);

    default TemplateKeyType getType() {
        return TemplateKeyType.EVALUATOR;
    }

}
