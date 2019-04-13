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
package tech.iooo.boot.core.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * ThreadPool
 *
 * @author Ivan97
 */
public interface ThreadPool {

  static ScheduledExecutorService scheduledExecutorService(ThreadPoolConfig config) {
    return new ScheduledThreadPoolExecutor(config.getCores(),
        new BasicThreadFactory.Builder().namingPattern(config.getNamePrefix())
            .daemon(config.isDaemon()).build());
  }

  /**
   * Thread pool
   *
   * @param config ThreadPoolConfig contains thread parameter
   * @return thread pool
   */
  ExecutorService executorService(ThreadPoolConfig config);

}
