package ua.kiev.its.assertstruct.impl.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateKeyType;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigTemplateKey extends TemplateKey {
    TemplateKeyType type;
    String propertyName;

    public ConfigTemplateKey(TemplateKeyType configType, @NonNull String value, String propertyName, ExtToken token) {
        super(value, token);
        this.propertyName = propertyName;
        this.type = configType;
    }

}
