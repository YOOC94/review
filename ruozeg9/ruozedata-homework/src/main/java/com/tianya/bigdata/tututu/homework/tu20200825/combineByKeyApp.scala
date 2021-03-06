package com.tianya.bigdata.tututu.homework.tu20200825

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * combineByKey算子的demo，也是combineByKeyWithClassTag的demo
 * 参考：https://www.edureka.co/blog/apache-spark-combinebykey-explained
 * https://blog.csdn.net/Gpwner/article/details/73349589
 *
 * 底层调用的是combineByKeyWithClassTag算子
 * createCombiner函数:有多少不同的key就调用多少次，相当于每个key的value组生成一个初始值
 *
 *
 */
object combineByKeyApp {


  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setMaster("local[3]").setAppName(this.getClass.getSimpleName)

    val sc: SparkContext = new SparkContext(conf)

    val scores = List(
      ScoreDetail("A","Math",98),
      ScoreDetail("A","English",88),
      ScoreDetail("B","Math",75),
      ScoreDetail("B","English",78),
      ScoreDetail("C","Math",90),
      ScoreDetail("C","English",80),
      ScoreDetail("D","Math",91),
      ScoreDetail("D","English",80),
    )

    val scoreWithKey = for (elem <- scores) yield (elem.studentName,elem)

    val scoreWithKeyRDD = sc.parallelize(scoreWithKey)
      .partitionBy(new HashPartitioner(3)).cache

    scoreWithKeyRDD.foreachPartition(partition => println("分区的长度："+partition.length))

    scoreWithKeyRDD.foreachPartition(partition => {
      println("这个是一个分区的数据：")
      partition.foreach(
      item => println(item._2)
    )})

    val avgScoreRDD = scoreWithKeyRDD.combineByKey(
      (x:ScoreDetail) => {
        println("createCombiner函数"+x.score)
        (x.score,1)
      },
      (acc:(Float,Int),x:ScoreDetail) => {
        println("mergeValue函数"+acc._1+":"+x.score)
        (acc._1+x.score,acc._2+1)
      },
      (acc1:(Float,Int),acc2:(Float,Int)) => {
        println("mergeCombiners函数"+acc1._1+":"+acc2._1)
        (acc1._1+acc2._1,acc1._2+acc2._2)
      }
    ).map({case(key,value) => (key,value._1/value._2)})
    avgScoreRDD.collect.foreach(println)
    sc.stop()
  }
}
case class ScoreDetail(studentName: String, subject: String, score: Float)
