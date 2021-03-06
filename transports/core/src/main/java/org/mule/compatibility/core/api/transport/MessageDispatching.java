/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.compatibility.core.api.transport;

import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.MuleMessage;
import org.mule.runtime.core.api.connector.DispatchException;

/**
 * Define generic methods for dispatching events. The exact behaviour of the action is defined by the implementing class.
 * 
 * @see org.mule.compatibility.core.api.endpoint.OutboundEndpoint
 * @see org.mule.compatibility.core.api.transport.MessageDispatcher
 * @deprecated Transport infrastructure is deprecated.
 */
@Deprecated
public interface MessageDispatching {

  long RECEIVE_WAIT_INDEFINITELY = 0;
  long RECEIVE_NO_WAIT = -1;

  /**
   * Dispatches an event from the endpoint to the external system
   * 
   * @param event The event to dispatch
   * @throws DispatchException if the event fails to be dispatched
   */
  void dispatch(MuleEvent event) throws DispatchException;

  /**
   * Sends an event from the endpoint to the external system
   * 
   * @param event The event to send
   * @return event the response form the external system wrapped in a MuleEvent
   * @throws DispatchException if the event fails to be dispatched
   */
  MuleMessage send(MuleEvent event) throws DispatchException;

}
