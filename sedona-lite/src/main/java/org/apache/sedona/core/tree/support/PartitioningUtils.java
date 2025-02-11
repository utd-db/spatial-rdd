package org.apache.sedona.core.tree.support;

import org.apache.sedona.core.enums.GridType;
import org.apache.sedona.core.tree.KDB;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class PartitioningUtils {
  // Check the geom against the org.apache.sedona.core.partition org.apache.sedona.core.tree to find the ids of overlapping grids
  public abstract Iterator<Tuple2<Integer, Geometry>> placeObject(Geometry geometry);
  // Check the geom against the org.apache.sedona.core.partition org.apache.sedona.core.tree to find the ids of overlapping grids. Only return IDs
  public abstract Set<Integer> getKeys(Geometry geometry);
  // Traverse the org.apache.sedona.core.partition org.apache.sedona.core.tree and fetch the grids
  public abstract List<Envelope> fetchLeafZones();

  public static PartitioningUtils getPartitioner(
      List<Geometry> samples, GridType gridType, Envelope boundaryEnvelope, int resolution) {
    List<Envelope> sampleEnvelopes = new ArrayList<Envelope>();
    for (Geometry geom : samples) {
      sampleEnvelopes.add(geom.getEnvelopeInternal());
    }
    // Add some padding at the top and right of the boundaryEnvelope to make
    // sure all geometries lie within the half-open rectangle.
    final Envelope paddedBoundary =
        new Envelope(
            boundaryEnvelope.getMinX(), boundaryEnvelope.getMaxX() + 0.01,
            boundaryEnvelope.getMinY(), boundaryEnvelope.getMaxY() + 0.01);
    PartitioningUtils partitioner = null;
    switch (gridType) {
      case KDBTREE:
        {
          final KDB tree = new KDB(sampleEnvelopes.size() / resolution, resolution, paddedBoundary);
          for (final Envelope sample : sampleEnvelopes) {
            tree.insert(sample);
          }
          tree.assignLeafIds();
          partitioner = tree;
          break;
        }
      default:
        try {
          throw new Exception(
              "[AbstractSpatialRDD][spatialPartitioning] Unsupported org.apache.sedona.core.spatial partitioning method. "
                  + "The following partitioning methods are not longer supported: R-Tree, Hilbert curve, Voronoi");
        } catch (Exception e) {
          e.printStackTrace();
          break;
        }
    }
    return partitioner;
  }

  /**
   * If the user only provides boundary and numPartitions, use equal grids.
   *
   * @param boundaryEnvelope
   * @param resolution
   * @return
   */
  public static PartitioningUtils getPartitioner(Envelope boundaryEnvelope, int resolution) {
    return getPartitioner(new ArrayList<>(), GridType.EQUALGRID, boundaryEnvelope, resolution);
  }
}
