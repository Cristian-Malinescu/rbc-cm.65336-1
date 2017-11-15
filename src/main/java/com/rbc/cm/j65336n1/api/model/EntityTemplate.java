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
import com.rbc.cm.j65336n1.api.model.Schemas.Entity.Meta.Fields;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
abstract public class EntityTemplate<E extends EntityTemplate<?>> implements Entity<E>,
                                                                             Fields {
  @Override
  @JsonProperty(ID)
  public Long id() { return _id; }

  private Long _id;

  @SuppressWarnings("unchecked")
  @Override
  @JsonProperty(ID)
  public E id(final Long val) {
    assert null != val;
    
    _id = val;
    
    return (E) this;
  }
  
  @Override
  public boolean equals(final Object o) {
    if (o instanceof EntityTemplate)
      return _id.equals(((EntityTemplate<?>) o)._id);
    
    return false;
  }

  @Override
  public int hashCode() { return _id.hashCode(); }
  
  protected EntityTemplate() {}

  protected EntityTemplate(final Long val) {
    id(val);
  }

  static final private long serialVersionUID = 1L;
}
