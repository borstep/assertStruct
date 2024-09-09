package org.assertstruct.template.node;

import lombok.experimental.Delegate;
import org.assertstruct.impl.opt.OptionsNode;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.template.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayNode extends LinkedList<TemplateNode> implements StructTemplateNode, DataNode<List<Object>> {

    @Delegate
    private final StructuredTemplateNodeShared shared;

    public ArrayNode(TemplateKey key, ExtToken startToken) {
        super();
        shared = new StructuredTemplateNodeShared(key, startToken, this);
    }

    @Override
    public boolean isArray() {
        return true;
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
                list.add(((DataNode<?>) child).toData());
        }
        return list;
    }
}
