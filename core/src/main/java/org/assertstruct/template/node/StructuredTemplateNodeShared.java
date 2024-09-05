package org.assertstruct.template.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.assertstruct.impl.opt.NodeOptions;
import org.assertstruct.impl.opt.OptionsNode;
import org.assertstruct.impl.opt.SubtreeOptions;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.impl.validator.TypeCheckValidator;
import org.assertstruct.opt.OptionsBuilder;
import org.assertstruct.service.SharedValidator;
import org.assertstruct.template.StructTemplateNode;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.TemplateNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class StructuredTemplateNodeShared {
    @Getter
    final TemplateKey key;
    @Getter
    final ExtToken startToken;
    final Iterable<TemplateNode> children;

    @Getter
    @Setter
    ExtToken token;

    @Getter
    NodeOptions config;
    NodeOptions.NodeOptionsBuilder configBuilder;

    @Getter
    SubtreeOptions subtreeOptions;
    @Getter
    SubtreeOptions.SubtreeOptionsBuilder subtreeConfigBuilder;

    Set<SharedValidator> _validators = null;

    @Getter
    Set<SharedValidator> validators = null;

    public void addSharedValidator(TypeCheckValidator typeValidator) {
        if (_validators == null) {
            _validators = new HashSet<>();
            validators = Collections.unmodifiableSet(_validators);
        }
        _validators.add(typeValidator);
    }

    public void addConfig(String name, TemplateNode value) {
        if (configBuilder == null)
            configBuilder = new NodeOptions.NodeOptionsBuilder();
        set(name, value, configBuilder);
    }

    public void addSubtreeConfig(String name, TemplateNode value) {
        if (subtreeConfigBuilder == null)
            subtreeConfigBuilder = new SubtreeOptions.SubtreeOptionsBuilder();
        set(name, value, subtreeConfigBuilder);
    }

    private static void set(String name, TemplateNode value, OptionsBuilder optionsBuilder) {
        if (value instanceof ObjectNode) {
            for (Map.Entry<String, TemplateNode> entry : ((ObjectNode) value).entrySet()) {
                set(entry.getKey(), entry.getValue(), optionsBuilder);
            }
        } else if (value instanceof OptionsNode) {
            optionsBuilder.set(((OptionsNode) value).getName(), ((OptionsNode) value).getValue());
        } else if (value instanceof ValueNode) {
            optionsBuilder.set(name, ((ValueNode) value).getValue());
        } else {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName() + " in configuration");
        }
    }

    public void sealConfigs(SubtreeOptions parentConfig) {
        if (subtreeConfigBuilder != null) {
            parentConfig = subtreeConfigBuilder.build(parentConfig);
        }
        boolean isDict = !(children instanceof ArrayNode);
        config = configBuilder == null ?
                NodeOptions.NodeOptionsBuilder.buildDefault(parentConfig, isDict) :
                configBuilder.build(parentConfig, isDict);
        for (TemplateNode child : children) {
            if (child.isStruct()) {
                ((StructTemplateNode) child).sealConfigs(parentConfig);
            }
        }
    }

    public boolean isOrdered() {
        return this.config.isOrdered();
    }

    public boolean isOrderedDicts() {
        return this.config.isOrderedDicts();
    }

    public boolean isOrderedLists() {
        return this.config.isOrderedLists();
    }

    public boolean isIgnoreUnknown() {
        return this.config.isIgnoreUnknown();
    }
}
