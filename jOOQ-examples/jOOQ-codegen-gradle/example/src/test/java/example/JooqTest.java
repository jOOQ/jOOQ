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

import java.util.List;

import com.google.inject.Inject;
import example.generated.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.modelmapper.ModelMapper;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static example.generated.tables.User.USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * @author Ben Manes (ben.manes@gmail.com)
 */
@Guice(modules = {JooqModule.class, H2Module.class})
public final class JooqTest {
  @Inject DSLContext db;
  @Inject ModelMapper mapper;
  RecordMapper<Record, User> recordMapper;

  @BeforeClass
  public void beforeClass() {
    recordMapper = new RecordMapper<Record, User>() {
      @Override public User map(Record record) {
        return mapper.map(record, User.class);
      }
    };
  }

  @AfterMethod
  public void afterMethod() {
    db.truncate(USER).execute();
  }

  @Test
  public void select() {
    User john = insertUser("John", "Doe");
    User user = db.selectFrom(USER)
        .where(USER.FIRST_NAME.equal("John"))
        .fetchOne().map(recordMapper);
    assertThat(user, is(john));
  }

  @Test
  public void selectAll() {
    User john = insertUser("John", "Doe");
    User jane = insertUser("Jane", "Doe");
    List<User> users = db.selectFrom(USER).fetch().map(recordMapper);
    assertThat(users, contains(john, jane));
  }

  @Test
  public void insert() {
    User john = insertUser("John", "Doe");
    assertThat(john.getId(), is(1));
  }

  @Test
  public void update() {
    User jane = insertUser("Jane", "Doe");
    jane.getName().setLastName("Smith");

    UserRecord record = mapper.map(jane, UserRecord.class);
    db.executeUpdate(record);

    User user = db.selectFrom(USER)
        .where(USER.LAST_NAME.equal("Smith"))
        .fetchOne().map(recordMapper);
    assertThat(user, is(jane));
  }

  private User insertUser(String firstName, String lastName) {
    UserRecord record = new UserRecord();
    record.setFirstName(firstName);
    record.setLastName(lastName);
    return db.insertInto(USER).set(record).returning().fetchOne().map(recordMapper);
  }
}
