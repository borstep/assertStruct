package org.assertstruct.template.node;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.impl.validator.TypeCheckValidator;
import org.assertstruct.matcher.ValueMatcher;
import org.assertstruct.service.SharedValidator;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;

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
