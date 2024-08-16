package ua.kiev.its.assertstruct.service;

import ua.kiev.its.assertstruct.utils.ResourceLocation;

public interface ResourceSourceLocator {
    ResourceLocation lookupSourceLocation(ResourceLocation location);
}
