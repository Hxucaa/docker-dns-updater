package dockerDNSUpdater.domain.CloudFlareDNS.common

/**
  * Created by Lance on 15/05/2017.
  */
import enumeratum._

sealed trait DNSType extends EnumEntry

object DNSType extends Enum[DNSType] with CirceEnum[DNSType] {

  /*
   `findValues` is a protected method that invokes a macro to find all `Greeting` object declarations inside an `Enum`

   You use it to implement the `val values` member
   */
  override val values = findValues

  case object A extends DNSType

  case object AAAA extends DNSType

  case object CNAME extends DNSType

  case object TXT extends DNSType

  case object SRV extends DNSType

  case object LOC extends DNSType

  case object MX extends DNSType

  case object NS extends DNSType

  case object SPF extends DNSType

}
