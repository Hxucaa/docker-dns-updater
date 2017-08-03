package dockerDNSUpdater.utility.mapping

import shapeless.labelled.FieldType

/**
  * Helper in mapping from one class to another.
  */
trait Field {
  type K
  type V
  type F = FieldType[K, V]

  val value: F
}

object Field {
  def apply[K0, V0](sample: FieldType[K0, V0]) = new Field {
    type K = K0
    type V = V0
    val value: F = sample
  }
}
