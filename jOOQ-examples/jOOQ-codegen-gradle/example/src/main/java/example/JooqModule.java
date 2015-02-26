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

import javax.sql.DataSource;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A module that configures JOOQ.
 *
 * @author Ben Manes (ben.manes@gmail.com)
 */
public final class JooqModule extends AbstractModule {
  private final SQLDialect dialect;
  private final Settings setting;

  public JooqModule() {
    this(SQLDialect.H2, new Settings().withRenderSchema(false));
  }

  public JooqModule(SQLDialect dialect, Settings setting) {
    this.dialect = checkNotNull(dialect);
    this.setting = checkNotNull(setting);
  }

  @Override
  protected void configure() {}

  @Provides @Singleton
  DSLContext providesDSLContext(DataSource dataSource) {
    return DSL.using(dataSource, dialect, setting);
  }
}
