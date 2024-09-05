package org.assertstruct.impl.factories.spel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.Parser;
import org.assertstruct.service.ParserFactory;
import org.assertstruct.service.exceptions.InitializationFailure;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
public class SpelFactory implements ParserFactory {
    public static final ParserFactory INSTANCE = new SpelFactory();

    @Override
    public Parser buildParser(AssertStructService assertStructService) {
        try {
            Class.forName("org.springframework.expression.spel.standard.SpelExpressionParser");
            Class<?> spelFactoryClass = Class.forName("org.assertstruct.impl.factories.spel.SpelParser");
            return (Parser) spelFactoryClass.newInstance();
        } catch (ClassNotFoundException ignore) { // Doesn't register if spring not found
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InitializationFailure(e);
        }
        return null;
    }
}
