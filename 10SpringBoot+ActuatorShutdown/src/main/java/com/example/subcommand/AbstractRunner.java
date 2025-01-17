/*
 * Copyright 2025 agwlvssainokuni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.subcommand;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractRunner implements ApplicationRunner, ExitCodeGenerator {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private final AtomicInteger exitCode = new AtomicInteger(0);

    public abstract int doRun(ApplicationArguments args) throws Exception;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            exitCode.set(doRun(args));
        } finally {
            countDownLatch.countDown();
        }
    }

    @Override
    public int getExitCode() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            // 何もしない
        }
        return exitCode.get();
    }

}
