package ua.kiev.its.assertstruct.template;

import ua.kiev.its.assertstruct.AssertStruct;
import ua.kiev.its.assertstruct.config.KeyFactory;
import ua.kiev.its.assertstruct.config.NodeFactory;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.impl.parser.JSon5Parser;
import ua.kiev.its.assertstruct.impl.validator.TypeCheckValidator;
import ua.kiev.its.assertstruct.template.node.ArrayNode;
import ua.kiev.its.assertstruct.template.node.ObjectNode;
import ua.kiev.its.assertstruct.template.node.StringNode;
import ua.kiev.its.assertstruct.template.node.ValueNode;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class TemplateParser {
    AssertStruct env;

    public TemplateParser() {
        this(AssertStruct.getDefaultInstance());
    }

    public TemplateParser(AssertStruct env) {
        this.env = env;
    }

    public Template parse(JSon5Parser parser) throws IOException {
        Deque nodes = new ArrayDeque();
        TemplateNode node = null;
        ExtToken token;
        TemplateKey currentKey = null;
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
                        node = (ArrayNode) nodes.pop();
                        ((ArrayNode) node).setToken(token);
                        break;
                    case END_OBJECT:
                        node = (ObjectNode) nodes.pop();
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
                    if (node == null)
                        throw new TemplateParseException("Can't parse template: empty template", parser.currentLocation());
//                    if (!(node instanceof StructTemplateNode))
//                        throw new TemplateParseException("Root node in template must be array or object");
                    return new Template(node, env);
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
    }


    private TemplateNode buildStringNode(TemplateKey templateKey, ExtToken token) {
        String value = (String) token.getValue();
        TemplateNode result = null;
        TypeCheckValidator typeValidator = null;
        if (token != null && !token.isDoubleQuoted()) {
            int typedIdx = value.indexOf(TypeCheckValidator.DELIMITER);
            if (typedIdx >= 0) {
                String className = value.substring(typedIdx + TypeCheckValidator.DELIMITER.length());
                if (TypeCheckValidator.isClassName(className)) {
                    value = value.substring(0, typedIdx);
                    typeValidator = createTypeValidator(className);
                }
            }
            for (NodeFactory nodeFactory : env.getNodeMatchingFactories()) {
                if (value.startsWith(nodeFactory.getPrefix())) {
                    result = nodeFactory.parseNode(value, templateKey, token);
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
                for (String pack : env.getDefaultPackages()) {
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
            for (KeyFactory keyFactory : env.getKeyMatchingFactories()) {
                if (key.startsWith(keyFactory.getPrefix())) {
                    TemplateKey templateKey = keyFactory.parseKey(key, keyToken);
                    if (templateKey != null)
                        return templateKey;
                }
            }
        }
        return new TemplateKey(key, keyToken);
    }

}
