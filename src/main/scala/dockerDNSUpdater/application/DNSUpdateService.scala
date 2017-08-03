package dockerDNSUpdater.application

import dockerDNSUpdater.domain.CloudFlareDNS.aggregate._
import dockerDNSUpdater.domain.CloudFlareDNS.common._
import dockerDNSUpdater.domain.CloudFlareDNS.command._
import dockerDNSUpdater.domain.DNSUpdate.DNSUpdateRepo

/**
  * Created by Lance on 11/05/2017.
  */
final class DNSUpdateService(private val config: DNSConfig,
                             private val dnsUpdateRepo: DNSUpdateRepo) {

  import DNSUpdateService._
  import fs2.Task

  def updateDNS(record: DNSInfo): Task[DNSRecord] = {
    dnsUpdateRepo.updateDNS(config.credential, config.zone, record)
  }
}

object DNSUpdateService {

  import shapeless._
  import shapeless.syntax.singleton._

  // cache Generic locally
  private lazy val genZoneId               = Generic[ZoneId]
  private lazy val genZone                 = Generic[Zone]
  private lazy val genCredential           = Generic[Credential]
  private lazy val genCloudFlareCredential = Generic[CloudFlareCredential]
  private lazy val genDNSInfo              = LabelledGeneric[DNSInfo]
  private lazy val genRecord               = LabelledGeneric[Record]

  private implicit def cloudFlareCredentialToCredential(
      credential: CloudFlareCredential): Credential =
    genCredential
      .from(genCloudFlareCredential.to(credential))

  private implicit def convertZone(zone: Zone): ZoneId =
    genZoneId
      .from(genZone.to(zone))

  private implicit def dnsInfoToRecord(record: DNSInfo): Record = {
    val dnstype: DNSType = DNSType.A
    val dnsInfoHList     = ('type ->> dnstype) :: genDNSInfo.to(record)

    genRecord.from(dnsInfoHList)
  }
}
