package com.github.tm.stindex.api;

import com.github.tm.stindex.ByteArray;
import com.github.tm.stindex.ByteArrayRange;
import org.junit.Test;

public class GeoIndexTest {

  private GeoIndex geoIndex = new GeoIndex(SpatialIndexType.Hilbert, 2);

  @Test
  public void getIndex() {
    ByteArray index = geoIndex.getIndex(-90, -45);
    System.out.println(index);
  }

  @Test
  public void decomposeRange() {
    ByteArrayRange[] ranges = geoIndex.decomposeRange(-90.f, 90.f, -45.f, 45.f);
    for (ByteArrayRange range : ranges) {
      System.out.println(range);
    }
  }

  @Test
  public void getFatherRange() {
    ByteArray index = geoIndex.getIndex(180, 90);
    ByteArrayRange childrenRange = new ByteArrayRange(index, index, true);
    ByteArrayRange fatherRange = geoIndex.getFatherRange(childrenRange);
    System.out.println(fatherRange);
  }

  @Test
  public void getRing() {
    ByteArray index = geoIndex.getIndex(-180, -90);
    ByteArrayRange centerRange = new ByteArrayRange(index, index, true);
    ByteArrayRange[] ring = geoIndex.getRing(centerRange);
    for (ByteArrayRange range : ring) {
      System.out.println(range);
    }
  }

  @Test
  public void getSpatialBoundingBoxTest() {
    ByteArray index = geoIndex.getIndex(-180, -90);
    BoundingBox bbox = geoIndex.getSpatialBoundingBox(index);
    System.out.println(bbox);
  }
}