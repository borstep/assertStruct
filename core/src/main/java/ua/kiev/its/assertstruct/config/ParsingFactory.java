package ua.kiev.its.assertstruct.config;

public interface ParsingFactory {
    /**
     * Order in which this factories will be used. Lower number means higher priority
     *
     * @return priority
     */
    default int priority() {
        return 0;
    }

    /**
     * Returns prefix to quick check if value can be processed by this factory
     *
     * @return prefix
     */
    String getPrefix();


}
