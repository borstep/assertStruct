package org.assertstruct.impl.factories.spel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.Parser;
import org.assertstruct.service.ParserFactory;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
public class SpelFactory implements ParserFactory {
    public static final ParserFactory INSTANCE = new SpelFactory();

    @Override
    public Parser buildParser(AssertStructService assertStructService) {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            classLoader.loadClass("org.springframework.expression.spel.standard.SpelExpressionParser");
            Class<?> spelFactoryClass = classLoader.loadClass("org.assertstruct.impl.factories.spel.SpelParser");
            SpelParser spelFactory = (SpelParser) spelFactoryClass.newInstance();
            return spelFactory;
        } catch (ClassNotFoundException ignore) { // Doesn't register if spring not found
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
