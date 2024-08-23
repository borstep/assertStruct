package org.assertstruct.template;

import org.assertstruct.matcher.Matcher;

public interface EvaluatorTemplateKey {
    Object evaluate(Object value, Matcher context);

    default TemplateKeyType getType() {
        return TemplateKeyType.EVALUATOR;
    }

}
