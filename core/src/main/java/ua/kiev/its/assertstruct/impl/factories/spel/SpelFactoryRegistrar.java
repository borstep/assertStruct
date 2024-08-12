package ua.kiev.its.assertstruct.impl.factories.spel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.AssertStruct;
import ua.kiev.its.assertstruct.config.ParsingFactoryRegistrar;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
public class SpelFactoryRegistrar implements ParsingFactoryRegistrar {
    public static final ParsingFactoryRegistrar INSTANCE = new SpelFactoryRegistrar();


    @Override
    public void registerFactory(AssertStruct env) {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            classLoader.loadClass("org.springframework.expression.spel.standard.SpelExpressionParser");
            Class<?> spelFactoryClass = classLoader.loadClass("ua.kiev.its.assertstruct.impl.factories.spel.SpelFactory");
            SpelFactory spelFactory = (SpelFactory) spelFactoryClass.newInstance();
            env.registerParsingFactory(spelFactory);
        } catch (ClassNotFoundException ignore) { // Doesn't register if spring not found
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
