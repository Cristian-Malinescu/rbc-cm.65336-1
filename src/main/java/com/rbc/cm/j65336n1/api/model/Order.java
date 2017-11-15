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

import javax.annotation.concurrent.NotThreadSafe;
import javax.money.MonetaryAmount;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.rbc.cm.j65336n1.api.model.Schemas.Order.Meta;
import com.rbc.cm.j65336n1.api.model.Schemas.Order.Meta.Fields;

import static com.rbc.cm.j65336n1.api.model.Direction.BUY;
import static com.rbc.cm.j65336n1.api.model.Direction.SELL;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@JsonRootName(Meta.ENTITY)
@NotThreadSafe
final public class Order extends EntityTemplate<Order> implements Fields {
  @JsonProperty(DIRECTION)
  public Direction direction() { return _direction; }

  private Direction _direction;

  @JsonProperty(DIRECTION)
  public Order direction(final Direction val) {
    assert null != val;
    _direction = val;
    return this;
  }

  @JsonProperty(RIC)
  public RIC ric() { return _ric; }

  private RIC _ric;

  @JsonProperty(RIC)
  public Order ric(final RIC val) {
    assert null != val;
    _ric = val;
    return this;
  }

  @JsonProperty(QUANTITY)
  public Integer quantity() { return _quantity; }

  private Integer _quantity;
  
  @JsonProperty(QUANTITY)
  public Order quantity(final Integer val) {
    assert null != val;
    _quantity = val;
    return this;
  }

  @JsonProperty(USER)
  public User user() { return _user; }

  private User _user;
  
  @JsonProperty(USER)
  public Order user(final User val) {
    assert null != val;
    _user = val;
    return this;
  }
  
  @JsonProperty(PRICE)
  public MonetaryAmount price() { return _price; }

  private MonetaryAmount _price;

  @JsonProperty(PRICE)
  public Order price(final MonetaryAmount val) {
    assert null != val;
    _price = val;
    return this;
  }

  @JsonProperty(STATE)
  public State state() { return _state; }
  
  private State _state;
  
  @JsonProperty(STATE)
  public Order state(final State val) {
    assert null != val;
    _state = val;
    return this;
  }
  
  @JsonProperty(TIMESTAMP)
  public LocalDateTime timeStamp() { return _timeStamp; }

  private LocalDateTime _timeStamp;

  @JsonProperty(TIMESTAMP)
  public Order timeStamp(final LocalDateTime val) {
    assert null != val;
    _timeStamp = val;
    return this;
  }

  /*
   * 
   **/
  public boolean match(final Order o) {
    if (null == o || o == this) return false;
    
    if (_ric.equals(o._ric) &&
        _quantity.intValue() == o.quantity().intValue() &&
        !_direction.equals(o._direction)) {
      final int x = _price.compareTo(o._price);
      
      return 0 == x ? true : x < 0 && SELL.equals(_direction)
                                         ||
                             x > 0 && BUY.equals(_direction);
    }
    
    return false;
  }

  /*
   * 
   **/
  static public boolean match(final Order a, final Order b) {
    return null == a || null == b || a == b ? false : a.match(b);
  }

  public Order init( final Long id,
                     final State state,
                     final LocalDateTime timestamp) {
    id(id);
    state(state);
    timeStamp(timestamp);
    return this;
  }

  static final public Comparator<Order> ByPriceMatcher = (a, b) -> {
    if (null == a || null == b) throw new IllegalArgumentException();
    if (a == b) return 0;
    if (!a.direction().equals(b.direction())) throw new IllegalArgumentException();
    return a.price().compareTo(b.price());
  };

  public Order() {/**/}
  
  public Order(final Long id) { super(id); }

  public Order(final Direction direction,
               final RIC ric,
               final Integer quantity,
               final MonetaryAmount price,
               final User user,
               final State state) {
    direction(direction).ric(ric)
                        .quantity(quantity)
                        .price(price)
                        .user(user)
                        .state(state);
  }

  public Order(final Order val) {
    assert null != val;
    direction(val.direction()).ric(val.ric())
                              .quantity(val.quantity())
                              .price(val.price())
                              .user(val.user())
                              .state(val.state());
  }
  
  /*
   * Search criteria helper
   **/
  private Order(final RIC ric, final Direction direction, final User user) {
    _ric = ric;
    _direction = direction;
    _user = user;
  }

  static Order build_filter_criteria(final RIC ric, final Direction direction, final User user) {
    return new Order(ric, direction, user);
  }

  static final private long serialVersionUID = 1L;
}
