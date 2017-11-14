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

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
public interface Schemas {
  /**
   * 
   */
  static interface Entity {
    interface Meta {
      static
        final
          public
            String ENTITY = "Entity";
      
      static interface Fields {
        static
          final
            public
              String ID = "ID";
      }
    }
  }
  
  /**
   * 
   */
  static interface Order {
    interface Meta {
      static
        final
          public
            String ENTITY = "Order";
      
      static interface Fields extends Entity.Meta.Fields {
        static
          final
            public
              String DIRECTION = "direction",
                     RIC = "RIC",
                     QUANTITY = "quantity",
                     PRICE = "price",
                     USER = "user",
                     STATE = "state",
                     TIMESTAMP = "timestamp";
      }
    }
  }

  /**
   * Reuters Instrument Code
   */
  interface RIC {
    interface Meta {
      static
        final
          public
            String ENTITY = "RIC";
          
      interface Fields {
        static
          final
            public
              String ROOT = "root",
                     EXCHANGE = "exchange";
      }
    }
  }
}
