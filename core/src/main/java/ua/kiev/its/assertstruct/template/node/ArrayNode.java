package ua.kiev.its.assertstruct.template.node;

import lombok.AccessLevel;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.impl.opt.OptionsNode;
import ua.kiev.its.assertstruct.impl.parser.ExtToken;
import ua.kiev.its.assertstruct.template.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArrayNode extends LinkedList<TemplateNode> implements StructTemplateNode, DataNode<List<Object>> {

    @Delegate
    StructuredTemplateNodeShared shared;

    public ArrayNode(TemplateKey key, ExtToken startToken) {
        super();
        shared = new StructuredTemplateNodeShared(key, startToken, this);
    }

    @Override
    public void printDebug(StringBuilder out) throws IOException {
        if (shared.startToken != null)
            shared.startToken.print(out, false, false);
        else
            out.append("[\n");
        for (TemplateNode node : this) {
            node.printDebug(out);
        }
        if (getToken() != null)
            getToken().print(out, false, false);
        else
            out.append("],\n");
    }

    @Override
    public boolean add(TemplateNode node) {
        if (node.isConfig()) {
            OptionsNode configNode = (OptionsNode) node;
            if (configNode.isSubTree()) {
                shared.addSubtreeConfig(configNode.getName(), configNode);
            } else {
                shared.addConfig(configNode.getName(), configNode);
            }
        }
        return super.add(node);
    }

    @Override
    public TemplateNode getByKey(Object key) {
        return get(((Number) key).intValue());
    }

    @Override
    public List<Object> toData() {
        ArrayList<Object> list = new ArrayList<>(size());
        for (TemplateNode child : this) {
            if ((child.getKey() == null || child.getKey().getType() == TemplateKeyType.SIMPLE) && child.isDataNode())
                list.add(((DataNode) child).toData());
        }
        return list;
    }
}
