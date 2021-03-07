package net;

import java.io.*;

public interface Unit extends Serializable {
  
  /**
   * Recomputes the value of this hidden unit, querying it's
   * prior inputs.
   */
  void recompute();
  
  /**
   * Returns the current value of this input
   *
   * @return The current value of this input
   */
  double getValue();
  
}