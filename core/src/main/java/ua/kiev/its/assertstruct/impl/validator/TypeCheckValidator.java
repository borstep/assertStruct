package ua.kiev.its.assertstruct.impl.validator;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import ua.kiev.its.assertstruct.config.SharedValidator;
import ua.kiev.its.assertstruct.matcher.Matcher;

import java.util.regex.Pattern;

@Value
@EqualsAndHashCode(callSuper = false)
public class TypeCheckValidator implements SharedValidator {
    public static final String DELIMITER = " ::";

    //    public static final Pattern REGEXP=Pattern.compile("::([A-Za-z_][A-Za-z0-9_.]*)"); //Java class regexp
    private static final String ID_PATTERN = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    private static final Pattern CLASS_NAME = Pattern.compile(ID_PATTERN + "(\\." + ID_PATTERN + ")*");

    public static boolean isClassName(String value) {
        return CLASS_NAME.matcher(value).matches();
    }

    @NonNull
    Class<?> clazz;

    @Override
    public boolean match(Object value, Matcher context) {
        Object sourceValue=context.getCurrentSource();
        return sourceValue != null && clazz.isAssignableFrom(sourceValue.getClass());
    }
}
