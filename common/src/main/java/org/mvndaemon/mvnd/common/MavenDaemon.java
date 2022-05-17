/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mvndaemon.mvnd.common;

import org.crac.Core;

public class MavenDaemon {

    public static void main(String[] args) throws Exception {
        ClassLoader loader = MavenDaemonClassLoader.getLoader();
        Thread.currentThread().setContextClassLoader(loader);
        Class<?> clazz = loader.loadClass("org.mvndaemon.mvnd.daemon.Server");
        try (AutoCloseable server = (AutoCloseable) clazz.getConstructor().newInstance()) {
            ((Runnable) server).run();
        }

        if (Environment.MVND_DO_CHECKPOINT.asBoolean()) {
            doCheckpoint();
        }
    }

    private static void doCheckpoint() {
        try {
            Core.checkpointRestore();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
