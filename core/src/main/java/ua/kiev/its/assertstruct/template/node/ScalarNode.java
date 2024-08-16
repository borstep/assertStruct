package ua.kiev.its.assertstruct.template.node;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.service.SharedValidator;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.impl.validator.TypeCheckValidator;
import ua.kiev.its.assertstruct.matcher.ValueMatcher;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ScalarNode implements TemplateNode, ValueMatcher {

    @Getter
    final TemplateKey key;

    @Getter
    final ExtToken token;

    Set<SharedValidator> _validators = null;

    @Getter
    Set<SharedValidator> validators = null;

    @Override
    public void addSharedValidator(TypeCheckValidator typeValidator) {
        if (_validators == null) {
            _validators = new HashSet<>();
            validators = Collections.unmodifiableSet(_validators);
        }
        _validators.add(typeValidator);
    }
}
