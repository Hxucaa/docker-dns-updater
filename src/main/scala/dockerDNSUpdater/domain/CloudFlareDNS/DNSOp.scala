package dockerDNSUpdater.domain.CloudFlareDNS

import dockerDNSUpdater.domain.CloudFlareDNS.command._
import common._
import aggregate.DNSRecord
import dockerDNSUpdater.domain.util.RefinedTypes._

/**
  * Created by Lance on 11/05/2017.
  */
sealed trait DNSOp[T]

object DNSOp {

  final case class Create(credential: Credential, record: Record) extends DNSOp[DNSRecord]

  final case class Get(credential: Credential, zoneId: ZoneId, dnsId: DNSId)
      extends DNSOp[DNSRecord]

  final case class GetRecordId(credential: Credential, zoneId: ZoneId, name: NonEmptyString)
      extends DNSOp[DNSId]

  final case class Update(credential: Credential,
                          zoneId: ZoneId,
                          dnsId: DNSId,
                          recordWithContent: RecordWithContent)
      extends DNSOp[DNSRecord]

}
