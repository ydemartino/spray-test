import akka.actor.{Actor, ActorLogging}
import akka.event.Logging._
import spray.http.HttpRequest
import spray.routing._
import spray.routing.directives.LogEntry
import spray.json._

class WHttpService extends Actor with HttpService with ActorLogging {

  implicit def actorRefFactory = context

  def receive = runRoute(route)

  lazy val route = logRequest(showReq _) {
    import MasterJsonProtocol._
    import spray.httpx.SprayJsonSupport._
    path("server" / Segment / DoubleNumber / DoubleNumber) { (login, first, second) =>
      get {
        complete {
          Answer(1, "test")
        }
      }
    }
  }

  private def showReq(req : HttpRequest) = LogEntry(req.uri, InfoLevel)
}

case class Answer(code: Int, content: String)
object MasterJsonProtocol extends DefaultJsonProtocol {
  implicit val anwserFormat = jsonFormat2(Answer)
}