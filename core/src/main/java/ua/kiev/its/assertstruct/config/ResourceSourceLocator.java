package ua.kiev.its.assertstruct.config;

import ua.kiev.its.assertstruct.utils.ResourceLocation;

public interface ResourceSourceLocator {
    ResourceLocation lookupSourceLocation(ResourceLocation location);
}
