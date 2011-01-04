/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.security;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.security.AuthenticationFilter;
import org.mule.api.security.CredentialsAccessor;
import org.mule.api.security.CryptoFailureException;
import org.mule.api.security.EncryptionStrategyNotFoundException;
import org.mule.api.security.SecurityException;
import org.mule.api.security.SecurityProviderNotFoundException;
import org.mule.api.security.UnknownAuthenticationTypeException;

/**
 * <code>AbstractEndpointSecurityFilter</code> provides a framework to perform inbound
 * or outbound authentication for messages.
 */

public abstract class AbstractAuthenticationFilter extends AbstractSecurityFilter implements AuthenticationFilter
{
    private boolean authenticate;
    protected MuleContext muleContext;
    private CredentialsAccessor credentialsAccessor;

    public CredentialsAccessor getCredentialsAccessor()
    {
        return credentialsAccessor;
    }

    public void setCredentialsAccessor(CredentialsAccessor credentialsAccessor)
    {
        this.credentialsAccessor = credentialsAccessor;
    }
    public boolean isAuthenticate()
    {
        return authenticate;
    }

    public void setAuthenticate(boolean authenticate)
    {
        this.authenticate = authenticate;
    }

    @Override
    public void doFilter(MuleEvent event)
        throws SecurityException, UnknownAuthenticationTypeException, CryptoFailureException,
        SecurityProviderNotFoundException, EncryptionStrategyNotFoundException, InitialisationException
    {
        authenticate(event);
    }

    public void authenticate(MuleEvent event)
            throws SecurityException, UnknownAuthenticationTypeException, CryptoFailureException,
            SecurityProviderNotFoundException, EncryptionStrategyNotFoundException,
            InitialisationException
    {
        if (event.getEndpoint() instanceof InboundEndpoint)
        {
            authenticateInbound(event);
        }
        else
        {
            authenticateOutbound(event);
        }
    }

    protected abstract void authenticateInbound(MuleEvent event)
            throws SecurityException, CryptoFailureException, SecurityProviderNotFoundException,
            EncryptionStrategyNotFoundException, UnknownAuthenticationTypeException;

    protected abstract void authenticateOutbound(MuleEvent event)
            throws SecurityException, SecurityProviderNotFoundException, CryptoFailureException;

    protected abstract void doInitialise() throws InitialisationException;

}
