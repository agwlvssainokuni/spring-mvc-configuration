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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Profile("subcommand")
@SpringBootConfiguration
@ComponentScan(basePackageClasses = SubCommand.class)
public class SubCommand {

    static final List<String> PROFILES = List.of(
            ShutdownRunner.PROFILE
    );

    public static Optional<Integer> execute(String[] args) {
        return Stream.of(args).findFirst().filter(PROFILES::contains).map(prof -> {
            try (var app = new SpringApplicationBuilder(SubCommand.class)
                    .profiles("subcommand", prof)
                    .run(args)) {
                return SpringApplication.exit(app);
            }
        });
    }

}
