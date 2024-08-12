package ua.kiev.its.assertstruct.impl.factories.spel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.matcher.Matcher;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.node.ScalarNode;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SpelNode extends ScalarNode {
    Expression expression;
    SpelFactory spelFactory;

    public SpelNode(Expression expression, TemplateKey key, ExtToken token, SpelFactory spelFactory) {
        super(key, token);
        this.expression = expression;
        this.spelFactory = spelFactory;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        StandardEvaluationContext sharedContext = spelFactory.getSharedContext();
        synchronized (sharedContext) {
            Object currentSource = context.getCurrentSource();
            Object parentSource = context.getParentSource();
            Object expectedValue = expression.getValue(sharedContext, parentSource);
            return Objects.equals(expectedValue, currentSource) || Objects.equals(expectedValue, value);
        }
    }

}
