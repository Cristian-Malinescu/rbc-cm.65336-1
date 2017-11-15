/*
 * Copyright 20017 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.rbc.cm.j65336n1.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
public class User {
  @JsonProperty
  private String id;

  public User() {/**/}

  public User(final String val) {
    setId(val);
  }

  public String getId() {
    return id;
  }

  public void setId(final String val) {
    id = val;
  }

}
