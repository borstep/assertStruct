package ua.kiev.its.assertstruct.impl.opt;

import ua.kiev.its.assertstruct.service.*;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.TemplateKey;
import ua.kiev.its.assertstruct.template.TemplateKeyType;
import ua.kiev.its.assertstruct.template.TemplateNode;
import ua.kiev.its.assertstruct.utils.ConversionUtils;

public class OptionsParser implements KeyParser, NodeParser, ParserFactory {
    public static final OptionsParser INSTANCE = new OptionsParser();
    public static final String PREFIX = "$opt";
    static final char CONFIG_VALUE_SEPARATOR = ':';
    static final String PREFIX_FIELD = "$opt.";
    static final String PREFIX_SUB_TREE = "$optSubtree";
    static final String PREFIX_SUB_TREE_FIELD = "$optSubtree.";

    @Override
    public TemplateKey parseKey(String value, ExtToken token) {
        if (value.equals(PREFIX)) {
            return new OptionsKey(TemplateKeyType.CONFIG_NODE, value, null, token);
        } else if (value.equals(PREFIX_SUB_TREE)) {
            return new OptionsKey(TemplateKeyType.CONFIG_SUB_TREE, value, null, token);
        } else if (value.startsWith(PREFIX_FIELD)) {
            return new OptionsKey(TemplateKeyType.CONFIG_FIELD, value, value.substring(PREFIX_FIELD.length()), token);
        } else if (value.startsWith(PREFIX_SUB_TREE_FIELD)) {
            return new OptionsKey(TemplateKeyType.CONFIG_SUB_TREE_FIELD, value, value.substring(PREFIX_SUB_TREE_FIELD.length()), token);
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
                if (namePart.startsWith(OptionsParser.PREFIX_FIELD)) {
                    name = namePart.substring(OptionsParser.PREFIX_FIELD.length());
                } else if (namePart.startsWith(OptionsParser.PREFIX_SUB_TREE_FIELD)) {
                    name = namePart.substring(OptionsParser.PREFIX_SUB_TREE_FIELD.length());
                    isSubTree = true;
                } else {
                    return null;
                }
                return new OptionsNode(name, ConversionUtils.convert(valueStr), isSubTree, token);
            }
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public Parser buildParser(AssertStructService assertStructService) {
        return INSTANCE;
    }
}
