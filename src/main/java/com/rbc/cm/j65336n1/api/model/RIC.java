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

import static java.util.Objects.hash;

import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.rbc.cm.j65336n1.api.model.Schemas.RIC.Meta;
import com.rbc.cm.j65336n1.api.model.Schemas.RIC.Meta.Fields;

/**
 * Reuters Instrument Code
 *
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@JsonRootName(Meta.ENTITY)
@NotThreadSafe
public class RIC implements Meta, Fields {
  @JsonProperty(ROOT)
  public String root() { return _root; }

  private String _root;

  @JsonProperty(ROOT)
  public RIC root(final String val) {
    assert null != val;
    _root = val;
    return this;
  }

  @JsonProperty(EXCHANGE)
  public String exchange() { return _exchange; }

  private String _exchange;

  @JsonProperty(EXCHANGE)
  public RIC exchange(final String val) {
    assert null != val;
    _exchange = val;
    return this;
  }
  
  @Override
  public boolean equals(final Object o) {
    if (o instanceof RIC) {
      final RIC r = (RIC) o;
      return _root.equals(r._root) && _exchange.equals(r._exchange);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return hash(_root, _exchange);
  }

  @Override
  public String toString() {
    return new StringBuilder(_root)
                 .append(".")
                 .append(_exchange)
                 .toString();
  }

  public RIC() {/**/}

  public RIC(final String root, final String exchange) {
    root(root);
    exchange(exchange);
  }

  public RIC(final RIC val) {
    assert null != val;
    root(val.root());
    exchange(val.exchange());
  }
}
