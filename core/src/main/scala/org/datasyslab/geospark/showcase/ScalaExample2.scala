package org.datasyslab.geospark.showcase

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.storage.StorageLevel
import org.datasyslab.geospark.enums.{FileDataSplitter, IndexType}
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator
import org.datasyslab.geospark.spatialRDD.PointRDD
import org.datasyslab.geospark.enums.GridType

object ScalaExample2 extends App {
  val conf = new SparkConf().setAppName("GeoSparkRunnableExample2").setMaster("yarn")
  conf.set("spark.serializer", classOf[KryoSerializer].getName)
  conf.set("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)

  val sc = new SparkContext(conf)
  Logger.getLogger("org").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)

  /*val resourceFolder = "/user/dreamlab/"//System.getProperty("user.dir") + "/src/test/resources/"
  val PointRDDInputLocation = resourceFolder + "asia2gb.csv"
  val PointRDDSplitter = FileDataSplitter.CSV
  val PointRDDIndexType = IndexType.RTREE
  val PointRDDNumPartitions = 5
  val PointRDDOffset = 0*/

  testequalgrid()
  sc.stop()
  System.out.println(" GeoSpark DEMO 1 passed!")
  /**
   * Test spatial range query.
   *
   * @throws Exception the exception
   */
  def testequalgrid() {

    val objectRDD = new PointRDD(sc, "/user/dreamlab/asia2gb.csv", 0, FileDataSplitter.CSV, true, StorageLevel.MEMORY_ONLY)
    objectRDD.rawSpatialRDD.persist(StorageLevel.MEMORY_ONLY)
/*
    objectRDD.spatialPartitioning(sc,GridType.EQUALGRID, 16)
    objectRDD.spatialPartitionedRDD.saveAsTextFile("/user/dreamlab/OUTPUT2GB/EQUALGRID")
*/

    /*objectRDD.spatialPartitioning(sc,GridType.RTREE, 16)
    objectRDD.spatialPartitionedRDD.saveAsTextFile("/user/dreamlab/OUTPUT2GB/RTREE")*/

    objectRDD.spatialPartitioning(sc,GridType.KDBTREE, 16)
    //objectRDD.spatialPartitionedRDD.saveAsTextFile("/user/dreamlab/OUTPUT2GB/KDBTREE")

    objectRDD.spatialPartitioning(sc,GridType.QUADTREE, 16)
    //objectRDD.spatialPartitionedRDD.saveAsTextFile("/user/dreamlab/OUTPUT2GB/QUADTREE")

    objectRDD.spatialPartitioning(sc,GridType.STR, 16)
    //objectRDD.spatialPartitionedRDD.saveAsTextFile("/user/dreamlab/OUTPUT2GB/STR")

  }


}
