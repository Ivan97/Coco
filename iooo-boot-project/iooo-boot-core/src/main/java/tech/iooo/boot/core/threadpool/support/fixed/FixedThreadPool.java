/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.iooo.boot.core.threadpool.support.fixed;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import tech.iooo.boot.core.threadlocal.NamedInternalThreadFactory;
import tech.iooo.boot.core.threadpool.ThreadPool;
import tech.iooo.boot.core.threadpool.ThreadPoolConfig;
import tech.iooo.boot.core.threadpool.support.AbortPolicyWithReport;

/**
 * Creates a thread pool that reuses a fixed number of threads
 *
 * @author Ivan97
 * @see java.util.concurrent.Executors#newFixedThreadPool(int)
 */
public class FixedThreadPool implements ThreadPool {

  private static final String FIXED_THREAD_NAME_PREFIX = "i-exec-fixed";
  private static final String SINGLE_THREAD_NAME_PREFIX = "i-exec-single";

  private ExecutorService executorService;

  @Override
  public ExecutorService executorService(ThreadPoolConfig config) {
    if (Objects.isNull(executorService)) {
      executorService = new ThreadPoolExecutor(config.getCores(), config.getCores(), 0L, TimeUnit.MILLISECONDS,
          config.getQueues() == 0 ? new SynchronousQueue<>() :
              (config.getQueues() < 0 ? new LinkedBlockingQueue<>()
                  : new LinkedBlockingQueue<>(config.getQueues())),
          new NamedInternalThreadFactory(
              config.getCores() == 1 ? SINGLE_THREAD_NAME_PREFIX : FIXED_THREAD_NAME_PREFIX,
              config.isDaemon()),
          new AbortPolicyWithReport());
    }
    return executorService;
  }

  public ExecutorService executorService() {
    return executorService(ThreadPoolConfig.DEFAULT_CONFIG
        .setCores(Runtime.getRuntime().availableProcessors() + 1)
        .setNamePrefix(FIXED_THREAD_NAME_PREFIX)
        .setQueues(Integer.MAX_VALUE));
  }
}
