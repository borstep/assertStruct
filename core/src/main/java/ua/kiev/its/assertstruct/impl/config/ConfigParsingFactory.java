package ua.kiev.its.assertstruct.impl.config;

import ua.kiev.its.assertstruct.config.KeyFactory;
import ua.kiev.its.assertstruct.config.NodeFactory;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateKeyType;
import ua.kiev.its.assertstruct.template.TemplateNode;
import ua.kiev.its.assertstruct.utils.ConversionUtils;

public class ConfigParsingFactory implements KeyFactory, NodeFactory {
    public static final ConfigParsingFactory INSTANCE = new ConfigParsingFactory();
    public static final String PREFIX = "$config";
    static final char CONFIG_VALUE_SEPARATOR = ':';
    static final String PREFIX_FIELD = "$config.";
    static final String PREFIX_SUB_TREE = "$configSubtree";
    static final String PREFIX_SUB_TREE_FIELD = "$configSubtree.";

    @Override
    public TemplateKey parseKey(String value, ExtToken token) {
        if (value.equals(PREFIX)) {
            return new ConfigTemplateKey(TemplateKeyType.CONFIG_NODE, value, null, token);
        } else if (value.equals(PREFIX_SUB_TREE)) {
            return new ConfigTemplateKey(TemplateKeyType.CONFIG_SUB_TREE, value, null, token);
        } else if (value.startsWith(PREFIX_FIELD)) {
            return new ConfigTemplateKey(TemplateKeyType.CONFIG_FIELD, value, value.substring(PREFIX_FIELD.length()), token);
        } else if (value.startsWith(PREFIX_SUB_TREE_FIELD)) {
            return new ConfigTemplateKey(TemplateKeyType.CONFIG_SUB_TREE_FIELD, value, value.substring(PREFIX_SUB_TREE_FIELD.length()), token);
        }
        return null;
//        throw new TemplateParseException("Can't parse template key: " + value, token.getLocation());
    }

    @Override
    public TemplateNode parseNode(String value, TemplateKey templateKey, ExtToken token) {
        if (value.startsWith(PREFIX)) {
            int separatorIdx = value.indexOf(CONFIG_VALUE_SEPARATOR);
            if (separatorIdx > 0) {
                String namePart = value.substring(0, separatorIdx).trim();
                String valueStr = value.substring(separatorIdx + 1).trim();
                String name = "";
                boolean isSubTree = false;
                if (namePart.startsWith(ConfigParsingFactory.PREFIX_FIELD)) {
                    name = namePart.substring(ConfigParsingFactory.PREFIX_FIELD.length());
                } else if (namePart.startsWith(ConfigParsingFactory.PREFIX_SUB_TREE_FIELD)) {
                    name = namePart.substring(ConfigParsingFactory.PREFIX_SUB_TREE_FIELD.length());
                    isSubTree = true;
                } else {
                    return null;
                }
                return new ConfigTemplateNode(name, ConversionUtils.convert(valueStr), isSubTree, token);
            }
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
