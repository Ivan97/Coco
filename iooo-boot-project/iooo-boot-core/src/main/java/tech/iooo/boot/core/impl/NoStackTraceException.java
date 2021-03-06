/*
 * Copyright (c) 2014 Red Hat, Inc. and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package tech.iooo.boot.core.impl;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class NoStackTraceException extends Throwable {

  private static final long serialVersionUID = -7472818745097794808L;

  public NoStackTraceException(String message) {
    super(message, null, false, false);
  }
}
