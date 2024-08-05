package ua.kiev.its.assertstruct.config;

public class ParsingFactoryComparator implements java.util.Comparator<ParsingFactory> {
    public static final ParsingFactoryComparator INSTANCE = new ParsingFactoryComparator();

    @Override
    public int compare(ParsingFactory o1, ParsingFactory o2) {
        int i = o1.priority() - o2.priority();
        if (i == 0) {
            return o1.equals(o2) ? 0 : -1;
        } else {
            return i;
        }
    }
}
