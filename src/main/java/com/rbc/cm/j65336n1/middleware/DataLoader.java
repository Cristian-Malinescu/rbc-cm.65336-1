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
package com.rbc.cm.j65336n1.middleware;

import static com.rbc.cm.j65336n1.api.model.Direction.BUY;
import static com.rbc.cm.j65336n1.api.model.Direction.SELL;
import static com.rbc.cm.j65336n1.api.model.State.OPEN;
import static java.time.LocalDateTime.now;
import static java.util.Optional.of;

import java.util.Optional;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;

import com.rbc.cm.j65336n1.api.model.Order;
import com.rbc.cm.j65336n1.api.model.RIC;
import com.rbc.cm.j65336n1.api.model.User;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
class DataLoader {

  void load(final boolean all) {
    final RIC ric = new RIC("VOD", "L");
    
    final User u1 = new User("User1"),
               u2 = new User("User2");
    
    Optional<Order> o;

    // 0
    o = _depo.create();

    o.ifPresent(val -> {
      val.ric(ric)
         .user(u1)
         .quantity(Integer.valueOf(1000))
         .price(monetary_amount(Float.valueOf(100.2f)))
         .direction(SELL)
         .state(OPEN)
         .timeStamp(now());
      _depo.update(of(val));
    });
    
    final int delay = 1000;

    // 1
    if (all) {
      try {
        Thread.sleep(delay);
      } catch (final InterruptedException e) {
        throw new IllegalStateException(e);
      }
      
      o = _depo.create();
  
      o.ifPresent(val -> {
        val.ric(ric)
           .user(u2)
           .quantity(Integer.valueOf(1000))
           .price(monetary_amount(Float.valueOf(100.2f)))
           .direction(BUY)
           .state(OPEN)
           .timeStamp(now());
        _depo.update(of(val));
      });
    }

    try {
      Thread.sleep(delay);
    } catch (final InterruptedException e) {
      throw new IllegalStateException(e);
    }
    
    // 2
    o = _depo.create();

    o.ifPresent(val -> {
      val.ric(ric)
         .user(u1)
         .quantity(Integer.valueOf(1000))
         .price(monetary_amount(Float.valueOf(99)))
         .direction(BUY)
         .state(OPEN)
         .timeStamp(now());
      _depo.update(of(val));
    });
    
    try { Thread.sleep(100); } catch (final InterruptedException e) { throw new IllegalStateException(e); }

    // 3
    o = _depo.create();

    o.ifPresent(val -> {
      val.ric(ric)
         .user(u1)
         .quantity(Integer.valueOf(1000))
         .price(monetary_amount(Float.valueOf(101)))
         .direction(BUY)
         .state(OPEN)
         .timeStamp(now());
      _depo.update(of(val));
    });
    
    try {
      Thread.sleep(delay);
    } catch (final InterruptedException e) {
      throw new IllegalStateException(e);
    }
    
    // 4
    o = _depo.create();

    o.ifPresent(val -> {
      val.ric(ric)
         .user(u2)
         .quantity(Integer.valueOf(500))
         .price(monetary_amount(Float.valueOf(102)))
         .direction(SELL)
         .state(OPEN)
         .timeStamp(now());
      _depo.update(of(val));
    });
        
    // 5
    if (all) {
      try {
        Thread.sleep(delay);
      } catch (final InterruptedException e) {
        throw new IllegalStateException(e);
      }
      
      o = _depo.create();
  
      o.ifPresent(val -> {
        val.ric(ric)
           .user(u1)
           .quantity(Integer.valueOf(500))
           .price(monetary_amount(Float.valueOf(103)))
           .direction(BUY)
           .state(OPEN)
           .timeStamp(now());
        _depo.update(of(val));
      });
    }
    
    
    
    // 6
    if (all) {
      try {
        Thread.sleep(delay);
      } catch (final InterruptedException e) {
        throw new IllegalStateException(e);
      }
      
      o = _depo.create();
  
      o.ifPresent(val -> {
        val.ric(ric)
           .user(u2)
           .quantity(Integer.valueOf(1000))
           .price(monetary_amount(Float.valueOf(98)))
           .direction(SELL)
           .state(OPEN)
           .timeStamp(now());
        _depo.update(of(val));
      });
    }
  }
  
  private CurrencyUnit currency_unit() {
    return Monetary.getCurrency("CAD");
  }

  private MonetaryAmount monetary_amount(final Number val) {
    return Money.of(val, currency_unit());
  }

  private final DataDepot<?> _depo;
  
  DataLoader(final DataDepot<?> val) {
    _depo = val;
  }
}
