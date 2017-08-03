/**
  * Created by Lance on 08/05/2017.
  */
import dockerDNSUpdater.DependencyManager
import dockerDNSUpdater.application._
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.refineMV

object Main extends App {

  private val name   = refineMV[NonEmpty]("nextcloud.cofree.pw")
  private val record = DNSInfo(name)

  val service = DependencyManager.configuredService

  val task = service.updateDNS(record)

  task.unsafeRun()
}
