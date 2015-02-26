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

import com.google.common.base.Objects;

/**
 * The application model of a user's name.
 *
 * @author Ben Manes (ben.manes@gmail.com)
 */
public final class Name {
  private String firstName;
  private String lastName;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof Name)) {
      return false;
    }
    Name name = (Name) o;
    return Objects.equal(firstName, name.firstName)
        && Objects.equal(lastName, name.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(firstName, lastName);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("firstName", firstName)
        .add("lastName", lastName)
        .toString();
  }
}
