package org.assertstruct.service;

import lombok.experimental.UtilityClass;

import java.util.TreeSet;

@UtilityClass
public class ConfigDocumentation {

    private static final String FORMAT = "| %-20s | %-60s |\n";

    public static void printConfigPropertyList() {
        System.out.printf(FORMAT, "Property", "Description");
        System.out.print(String.format(FORMAT, "", "").replace(' ', '-'));
        for (String name : new TreeSet<>(AssertStructConfigLoader.setters.keySet())) {
            System.out.printf(FORMAT, name, "");
        }
    }

    public static void main(String[] args) {
        ConfigDocumentation.printConfigPropertyList();
    }
}
