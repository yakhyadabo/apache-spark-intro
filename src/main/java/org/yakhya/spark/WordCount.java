package org.yakhya.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Created by ydabo on 09/06/17.
 */
public class WordCount {

  public static void wordCountJava8( String filename )
  {
    // Define a configuration to use to interact with Spark
    SparkConf conf = new SparkConf()
        .setMaster("local")
        .setAppName("Work Count App");

    // Create a Java version of the Spark Context from the configuration
    JavaSparkContext sc = new JavaSparkContext(conf);

    // Load the input data, which is a text file read from the command line
    JavaRDD<String> input = sc.textFile( filename );

    // Java 8 with lambdas: split the input string into words
    JavaRDD<String> words = input.flatMap( s -> Arrays.asList( s.split( " " ) ) );

    // Java 8 with lambdas: transform the collection of words into pairs (word and 1) and then count them
    JavaPairRDD<String, Integer> counts = words
        .mapToPair(t -> new Tuple2( t, 1 ) )
        .reduceByKey( (x, y) -> (int)x + (int)y );

    // Save the word count back out to a text file, causing evaluation.
    counts.saveAsTextFile( "output" );
  }

  public static void main( String[] args )
  {
    if( args.length == 0 )
    {
      System.out.println( "Usage: WordCount <file>" );
      System.exit( 0 );
    }

    wordCountJava8( args[ 0 ] );
  }

}