package org.assertstruct.service;

public class ParsingFactoryComparator implements java.util.Comparator<Parser> {
    public static final ParsingFactoryComparator INSTANCE = new ParsingFactoryComparator();

    @Override
    public int compare(Parser o1, Parser o2) {
        int i = o1.priority() - o2.priority();
        if (i == 0) {
            return o1.equals(o2) ? 0 : -1;
        } else {
            return i;
        }
    }
}
