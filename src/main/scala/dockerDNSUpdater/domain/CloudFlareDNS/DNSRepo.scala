package dockerDNSUpdater.domain.CloudFlareDNS

import aggregate._
import command._
import common._
import DNSOp._
import dockerDNSUpdater.domain.util.RefinedTypes._
import cats.free.Free.inject
import cats.free.{Free, Inject}

/**
  * Created by Lance on 11/05/2017.
  */
class DNSRepo[F[_]](implicit I: Inject[DNSOp, F]) {

  type DNSRepoF[T] = Free[F, T]

  def create(credential: Credential, record: Record): DNSRepoF[DNSRecord] =
    inject(Create(credential, record))

  def get(credential: Credential, zoneId: ZoneId, dnsId: DNSId): DNSRepoF[DNSRecord] =
    inject(Get(credential, zoneId, dnsId))

  def getRecordId(credential: Credential, zoneId: ZoneId, name: NonEmptyString): DNSRepoF[DNSId] =
    inject(GetRecordId(credential, zoneId, name))

  def update(credential: Credential,
             zoneId: ZoneId,
             dnsId: DNSId,
             recordWithContent: RecordWithContent): DNSRepoF[DNSRecord] =
    inject(Update(credential, zoneId, dnsId, recordWithContent))
}

object DNSRepo {
  implicit def imp[F[_]](implicit I: Inject[DNSOp, F]): DNSRepo[F] =
    new DNSRepo[F]
}

//}
//object DNSRepo {
//
//  import dockerDNSUpdater.domain.DNS.aggregate._
//  import dockerDNSUpdater.interpreter.IPv4Address
//  import dockerDNSUpdater.interpreter.DNSOp
//  import dockerDNSUpdater.interpreter.DNSOp._
//  import dockerDNSUpdater.interpreter.Http4sInterpreter
//
//  import fs2.interop.cats._
//  import fs2.Task
//  import cats.free.Free
//  import cats.free.Free.liftF
//
//  type DNSUpdateF[T] = Free[DNSOp, T]
//
//  // TODO: maybe wrap Free in a structure???
//
//  def getPublicIPAddress: DNSUpdateF[IPv4Address] = liftF(GetPublicIP)
//
//  def create(credential: Credential,
//             record: Record): DNSUpdateF[DNSRecord] =
//    liftF(Create(credential, record))
//
//  def get(credential: Credential,
//          zoneIdentifier: String,
//          dnsIdentifier: String): DNSUpdateF[DNSRecord] =
//    liftF(Get(credential, zoneIdentifier, dnsIdentifier))
//
//  def getRecordId(credential: Credential,
//                 zoneIdentifier: String,
//                 name: String): DNSUpdateF[String] =
//    liftF(GetRecordId(credential, zoneIdentifier, name))
//
//  def update(credential: Credential,
//             zoneIdentifier: String,
//             dnsIdentifier: String,
//             recordWithContent: RecordWithContent): DNSUpdateF[DNSRecord] =
//    liftF(Update(credential, zoneIdentifier, dnsIdentifier, recordWithContent))
//
//  def run[T](program: DNSUpdateF[T]): Task[T] = program.foldMap(Http4sInterpreter)
//}
