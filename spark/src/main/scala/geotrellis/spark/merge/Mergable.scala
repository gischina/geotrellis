package geotrellis.spark.merge

trait Mergable[T] {
  def merge(t1: T, t2: T): T
}
