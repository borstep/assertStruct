package ua.kiev.its.assertstruct.template;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;

import java.util.Objects;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TemplateKey {
    @NonNull
    String value;
    ExtToken token;


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof TemplateKey)
            return Objects.equals(((TemplateKey) o).value, value);
        if (o instanceof String)
            return Objects.equals(o, value);
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public TemplateKeyType getType() {
        return TemplateKeyType.SIMPLE;
    }
}
