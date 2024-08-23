package org.assertstruct.impl.opt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateKeyType;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OptionsKey extends TemplateKey {
    TemplateKeyType type;
    String propertyName;

    public OptionsKey(TemplateKeyType configType, @NonNull String value, String propertyName, ExtToken token) {
        super(value, token);
        this.propertyName = propertyName;
        this.type = configType;
    }

}
