package dockerDNSUpdater.domain.CloudFlareDNS.command

import dockerDNSUpdater.domain.util.RefinedTypes._

/**
  * Created by Lance on 09/06/2017.
  */
final case class Credential(email: NonEmptyString, key: NonEmptyString)
