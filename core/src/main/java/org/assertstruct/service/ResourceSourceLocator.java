package org.assertstruct.service;

import org.assertstruct.utils.ResourceLocation;

public interface ResourceSourceLocator {
    ResourceLocation lookupSourceLocation(ResourceLocation location);
}
