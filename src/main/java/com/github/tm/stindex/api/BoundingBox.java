package com.github.tm.stindex.api;

import com.github.tm.stindex.temporal.data.TimeRange;

/**
 * @author Yu Liebing
 */
public class BoundingBox {
  private TimeRange timeRange;
  private boolean hasTime = false;
  private double minLat;
  private double maxLat;
  private double minLon;
  private double maxLon;

  public BoundingBox(double minLat, double maxLat, double minLon, double maxLon) {
    this.minLat = minLat;
    this.maxLat = maxLat;
    this.minLon = minLon;
    this.maxLon = maxLon;
  }

  public BoundingBox(TimeRange timeRange, double minLat, double maxLat, double minLon, double maxLon) {
    this.timeRange = timeRange;
    hasTime = true;
    this.minLat = minLat;
    this.maxLat = maxLat;
    this.minLon = minLon;
    this.maxLon = maxLon;
  }

  @Override
  public String toString() {
    String timeStr = hasTime ? "time range: " + timeRange + ", " : "";
    return timeStr + "latitude range: [" + minLat + ", " +
            maxLat + "], longitude range: [" + minLon + ", " + maxLon + "]";
  }
}