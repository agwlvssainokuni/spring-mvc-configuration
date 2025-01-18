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

package com.example.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class HomeServiceImpl implements HomeService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public HomeServiceImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Transactional
    @Override
    public void home() {
        var currentTimestamp = namedParameterJdbcOperations.queryForObject(
                "SELECT CURRENT_TIMESTAMP FROM dual",
                new EmptySqlParameterSource(),
                LocalDateTime.class
        );
        logger.info("HomeService#home invoked, {}", currentTimestamp);
    }

}
