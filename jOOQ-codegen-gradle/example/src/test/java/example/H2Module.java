/*
 * Copyright 2013 Ben Manes. All Rights Reserved.
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
package example;

import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.googlecode.flyway.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

/**
 * A module that configures the H2 data source.
 *
 * @author Ben Manes (ben.manes@gmail.com)
 */
public class H2Module extends AbstractModule {
  private static final String URL = "jdbc:h2:mem:test_%d;DB_CLOSE_DELAY=-1";
  private static final AtomicInteger counter = new AtomicInteger();

  @Override
  protected void configure() {}

  @Provides
  DataSource providesDataSource() {
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL(String.format(URL, counter.incrementAndGet()));

    Flyway flyway = new Flyway();
    flyway.setDataSource(dataSource);
    flyway.migrate();

    return dataSource;
  }
}
