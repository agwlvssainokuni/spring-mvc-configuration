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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Profile(ShutdownRunner.PROFILE)
@Component
public class ShutdownRunner extends AbstractRunner {

    public static final String PROFILE = "shutdown";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String baseUri;
    private final String shutdownPath;

    public ShutdownRunner(
            @Value("${shutdown.base-uri}") String baseUri,
            @Value("${shutdown.path}") String shutdownPath
    ) {
        this.baseUri = baseUri;
        this.shutdownPath = shutdownPath;
    }

    @Override
    public int doRun(ApplicationArguments args) {
        try {
            var response = RestClient.create(baseUri)
                    .post().uri(shutdownPath)
                    .retrieve().body(String.class);
            logger.info("response: {}", response);
            return 0;
        } catch (ResourceAccessException ex) {
            logger.error("failed to call shutdown", ex);
            return 1;
        }
    }

}
