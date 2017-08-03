package dockerDNSUpdater.utility.Logging.interpreter

import dockerDNSUpdater.utility.Logging.LoggingOp
import cats.{Id, ~>}

final class LoggingPrintSyncI extends (LoggingOp ~> Id) {

  import LoggingOp._

  override def apply[A](fa: LoggingOp[A]): Id[A] = fa match {
    case Debug(msg)                 => println(msg)
    case DebugWithCause(msg, cause) => println(s"${msg}\n${cause}")
    case Error(msg)                 => println(msg)
    case ErrorWithCause(msg, cause) => println(s"${msg}\n${cause}")
    case Info(msg)                  => println(msg)
    case InfoWithCause(msg, cause)  => println(s"${msg}\n${cause}")
    case Warn(msg)                  => println(msg)
    case WarnWithCause(msg, cause)  => println(s"${msg}\n${cause}")
  }
}
