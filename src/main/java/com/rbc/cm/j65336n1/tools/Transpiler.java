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
package com.rbc.cm.j65336n1.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@Component
public class Transpiler {

  public <T> String transpile(final T val) {
    try { return _om.writeValueAsString(val); }
      catch (final JsonProcessingException e) {throw new IllegalStateException(e); }
  }
  
  public <T> T transpile(final Class<T> type, final String str) {
    try { return _om.readValue(str, type); }
      catch (final IOException e) {throw new IllegalStateException(e); }
  }

  @Autowired
  private ObjectMapper _om;
  
  public Transpiler() {}
}
