package ua.kiev.its.assertstruct.template.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ua.kiev.its.assertstruct.config.ConfigBuilder;
import ua.kiev.its.assertstruct.config.SharedValidator;
import ua.kiev.its.assertstruct.impl.config.ConfigTemplateNode;
import ua.kiev.its.assertstruct.impl.config.NodeConfig;
import ua.kiev.its.assertstruct.impl.config.SubtreeConfig;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.impl.validator.TypeCheckValidator;
import ua.kiev.its.assertstruct.template.StructTemplateNode;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateNode;

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
    NodeConfig config;
    NodeConfig.NodeConfigBuilder configBuilder;

    @Getter
    SubtreeConfig subtreeConfig;
    @Getter
    SubtreeConfig.SubtreeConfigBuilder subtreeConfigBuilder;

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
            configBuilder = new NodeConfig.NodeConfigBuilder();
        set(name, value, configBuilder);
    }

    public void addSubtreeConfig(String name, TemplateNode value) {
        if (subtreeConfigBuilder == null)
            subtreeConfigBuilder = new SubtreeConfig.SubtreeConfigBuilder();
        set(name, value, subtreeConfigBuilder);
    }

    private static void set(String name, TemplateNode value, ConfigBuilder configBuilder) {
        if (value instanceof ObjectNode) {
            for (Map.Entry<String, TemplateNode> entry : ((ObjectNode) value).entrySet()) {
                set(entry.getKey(), entry.getValue(), configBuilder);
            }
        } else if (value instanceof ConfigTemplateNode) {
            configBuilder.set(((ConfigTemplateNode) value).getName(), ((ConfigTemplateNode) value).getValue());
        } else if (value instanceof ValueNode) {
            configBuilder.set(name, ((ValueNode) value).getValue());
        } else {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName() + " in configuration");
        }
    }

    public void sealConfigs(SubtreeConfig parentConfig) {
        if (subtreeConfigBuilder != null) {
            parentConfig = subtreeConfigBuilder.build(parentConfig);
        }
        boolean isDict = !(children instanceof ArrayNode);
        config = configBuilder == null ?
                NodeConfig.NodeConfigBuilder.buildDefault(parentConfig, isDict) :
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
