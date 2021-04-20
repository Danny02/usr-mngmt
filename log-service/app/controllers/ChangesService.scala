package controllers

import play.api.libs.json.JsArray
import play.api.libs.ws.WSClient

import java.time.Instant
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ChangesService @Inject()(ws: WSClient, implicit val ec: ExecutionContext) {
  val serviceBaseUrl = "http://localhost:8080"
  val idPattern = """/user/([^/]+)/changes""".r

  def changesOverview() = {
    val changesUrl = ws.url(serviceBaseUrl).get().map { resp =>
      (resp.json \ "_links" \ "changes" \ "href").as[String]
    }

    changesUrl.flatMap(path => ws.url(serviceBaseUrl + path).get()).map { resp =>
      val item = (resp.json \ "_embedded" \ "item").get
      item match {
        case JsArray(items) => items.map { i =>
          val idPattern(id) = (i \ "_links" \ "changes" \ "href").as[String]
          UserChanges(
            (i \ "pseudonym").as[String],
            (i \ "eventCount").as[Int],
            id
          )
        }
      }
    }
  }

  def changes(id: String) = {
    ws.url(s"$serviceBaseUrl/user/$id/changes").get().map{resp =>
      (resp.json \ "changes").get match {
        case JsArray(items) => items.map{i =>
          Change(
            (i \ "instant").as[Instant],
            (i \ "event").as[String],
            (i \ "pseudonym").as[String]
          )
        }
      }
    }
  }
}

case class UserChanges(pseudonym: String, eventCount: Int, id: String)
case class Change(instant: Instant, event: String, pseudonym: String)
