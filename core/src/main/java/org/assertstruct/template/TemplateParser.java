package org.assertstruct.template;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertstruct.AssertStruct;
import org.assertstruct.impl.opt.SubtreeOptions;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.impl.parser.JSon5Parser;
import org.assertstruct.impl.validator.TypeCheckValidator;
import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.Config;
import org.assertstruct.service.KeyParser;
import org.assertstruct.service.NodeParser;
import org.assertstruct.template.node.ArrayNode;
import org.assertstruct.template.node.ObjectNode;
import org.assertstruct.template.node.StringNode;
import org.assertstruct.template.node.ValueNode;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemplateParser {
    final AssertStructService env;
    final Config config;

    final Deque<TemplateNode> nodes = new ArrayDeque<>();
    TemplateKey currentKey;

    public TemplateParser() {
        this(AssertStruct.getDefault());
    }

    public TemplateParser(AssertStructService env) {
        this.env = env;
        this.config = env.getConfig();
    }

    public Template parse(JSon5Parser parser) throws IOException {
        try {
            TemplateNode node;
            ExtToken token;
            while ((token = parser.next()) != null) {
                try {
                    switch (token.getType()) {
                        case START_OBJECT:
                            node = new ObjectNode(currentKey, token);
                            currentKey = null;
                            nodes.push(node);
                            continue;
                        case START_ARRAY:
                            node = new ArrayNode(currentKey, token);
                            currentKey = null;
                            nodes.push(node);
                            continue;
                        case FIELD_NAME:
                            currentKey = buildKey(token);
                            continue;
                        case END_ARRAY:
                            node = nodes.pop();
                            ((ArrayNode) node).setToken(token);
                            break;
                        case END_OBJECT:
                            node = nodes.pop();
                            ((ObjectNode) node).setToken(token);
                            break;
                        case VALUE_STRING:
                            node = buildStringNode(currentKey, token);
                            currentKey = null;
                            break;
                        default:
                            if (token.getType().isScalarValue()) {
                                node = new ValueNode(currentKey, token.getValue(), token);
                                currentKey = null;
                            } else {
                                throw new TemplateParseException("Unexpected token: " + token, token.getLocation());
                            }
                    }
                    if (nodes.isEmpty()) {
                        if (node instanceof StructTemplateNode) {
                            ((StructTemplateNode) node).sealConfigs(SubtreeOptions.INITIAL);
                        }
                        return new Template(node);
                    } else if (nodes.peek() instanceof ObjectNode) { // object
                        ((ObjectNode) nodes.peek()).put(node.getKey().getValue(), node); //TODO
                    } else if (nodes.peek() instanceof ArrayNode) { // array
                        ((ArrayNode) nodes.peek()).add(node);
                    } else {
                        throw new TemplateParseException("Unexpected token: " + token, token.getLocation());
                    }
                } catch (TemplateParseException e) {
                    throw e;
                } catch (Throwable e) {
                    throw new TemplateParseException(e, parser.currentLocation());
                }
            }
            throw new TemplateParseException("Can't parse template: unexpected end of stream");
        } finally {
            nodes.clear();
            currentKey = null;
        }
    }

    public StructTemplateNode currentNode() {
        return nodes.isEmpty() ? null : (StructTemplateNode) nodes.peek();
    }

    private TemplateNode buildStringNode(TemplateKey templateKey, ExtToken token) {
        String value = (String) token.getValue();
        TemplateNode result = null;
        TypeCheckValidator typeValidator = null;
        if (!token.isDoubleQuoted()) {
            int typedIdx = value.indexOf(TypeCheckValidator.DELIMITER);
            if (typedIdx >= 0) {
                String className = value.substring(typedIdx + TypeCheckValidator.DELIMITER.length());
                if (TypeCheckValidator.isClassName(className)) {
                    value = value.substring(0, typedIdx);
                    typeValidator = createTypeValidator(className);
                }
            }
            for (NodeParser nodeParser : env.getNodeParsers()) {
                if (value.startsWith(nodeParser.getPrefix())) {
                    result = nodeParser.parseNode(value, templateKey, token, this);
                    if (result != null)
                        break;
                }
            }
        }
        if (result == null)
            result = new StringNode(templateKey, value, token);
        if (typeValidator != null)
            result.addSharedValidator(typeValidator);
        return result;
    }

    TypeCheckValidator createTypeValidator(String className) {
        try {
            Class<?> aClass = null;
            if (className.contains(".")) {
                aClass = this.getClass().getClassLoader().loadClass(className);

            } else {
                for (String pack : config.getDefaultPackages()) {
                    try {
                        aClass = this.getClass().getClassLoader().loadClass(pack + "." + className);
                    } catch (ClassNotFoundException ignore) {
                    }
                }
                if (aClass == null)
                    throw new TemplateParseException("Can't parse template, unknown class: " + className);

            }
            return new TypeCheckValidator(aClass);
        } catch (ClassNotFoundException e) {
            throw new TemplateParseException("Can't parse template, unknown class: " + className, e);
        }
    }

    private TemplateKey buildKey(ExtToken keyToken) {
        String key = (String) keyToken.getValue();
        if (!keyToken.isDoubleQuoted()) {
            for (KeyParser keyParser : env.getKeyParsers()) {
                if (key.startsWith(keyParser.getPrefix())) {
                    TemplateKey templateKey = keyParser.parseKey(key, keyToken);
                    if (templateKey != null)
                        return templateKey;
                }
            }
        }
        return new TemplateKey(key, keyToken);
    }

}
