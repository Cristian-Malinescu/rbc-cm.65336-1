package com.rbc.cm.j65336n1.api.model;

final public class Utils {
  
  static public RIC parse(final String str) { return new RIC(str); }
  
  static public Order build_filter_criteria(final RIC ric, final Direction direction, final User user) {
    return Order.build_filter_criteria(ric, direction, user);
  }

  private Utils() {/**/}
}
