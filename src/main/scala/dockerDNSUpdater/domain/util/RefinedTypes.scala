package dockerDNSUpdater.domain.util

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Positive

/**
  * Created by Lance on 11/06/2017.
  */
object RefinedTypes {
  type NonEmptyString = String Refined NonEmpty
  type PositiveInt    = Int Refined Positive
}
