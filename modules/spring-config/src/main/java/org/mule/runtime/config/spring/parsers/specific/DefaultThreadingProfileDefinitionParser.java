/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.config.spring.parsers.specific;

import org.mule.runtime.core.api.config.ThreadingProfile;
import org.mule.runtime.core.config.ChainedThreadingProfile;
import org.mule.runtime.config.spring.parsers.generic.OrphanDefinitionParser;
import org.mule.runtime.config.spring.parsers.processors.IdAttribute;
import org.mule.runtime.config.spring.parsers.processors.NameAttribute;

/**
 * This parser is responsible for processing the <code><threading-profile><code> configuration elements.
 */
public class DefaultThreadingProfileDefinitionParser extends OrphanDefinitionParser {

  public DefaultThreadingProfileDefinitionParser(String propertyName) {
    super(ChainedThreadingProfile.class, true);
    addMapping("poolExhaustedAction", ThreadingProfile.POOL_EXHAUSTED_ACTIONS);
    registerPostProcessor(new IdAttribute(propertyName));
    registerPostProcessor(new NameAttribute(propertyName));
  }

}
