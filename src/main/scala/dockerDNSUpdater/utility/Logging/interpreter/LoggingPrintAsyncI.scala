package dockerDNSUpdater.utility.Logging.interpreter

import dockerDNSUpdater.utility.Logging.LoggingOp
import cats.~>
import fs2.Task

final class LoggingPrintAsyncI extends (LoggingOp ~> Task) {

  import LoggingOp._

  override def apply[A](fa: LoggingOp[A]): Task[A] = fa match {
    case Debug(msg)                 => Task.delay(println(msg))
    case DebugWithCause(msg, cause) => Task.delay(println(s"${msg}\n${cause}"))
    case Error(msg)                 => Task.delay(println(msg))
    case ErrorWithCause(msg, cause) => Task.delay(println(s"${msg}\n${cause}"))
    case Info(msg)                  => Task.delay(println(msg))
    case InfoWithCause(msg, cause)  => Task.delay(println(s"${msg}\n${cause}"))
    case Warn(msg)                  => Task.delay(println(msg))
    case WarnWithCause(msg, cause)  => Task.delay(println(s"${msg}\n${cause}"))
  }
}
