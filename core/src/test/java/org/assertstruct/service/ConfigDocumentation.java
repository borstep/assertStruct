package org.assertstruct.service;

import lombok.experimental.UtilityClass;
import org.apache.commons.beanutils.BeanUtils;
import org.assertstruct.AssertStruct;

import java.util.TreeSet;

@UtilityClass
public class ConfigDocumentation {

    private static final String FORMAT = "| %-27s | %-60s | %-20s |\n";

    public static void printConfigPropertyList() {
        System.out.printf(FORMAT, "Property", "Description", "Default value");
        System.out.print(String.format(FORMAT, "", "", "").replace(' ', '-'));
        Config config = AssertStruct.getDefault().getConfig();
        for (String name : new TreeSet<>(AssertStructConfigLoader.setters.keySet())) {
            String defaultValue = "";
            try {
                defaultValue = BeanUtils.getProperty(config, name);
            } catch (Exception ignore) {}
            System.out.printf(FORMAT, name, "", defaultValue);
        }
    }

    public static void main(String[] args) {
        ConfigDocumentation.printConfigPropertyList();
    }
}
