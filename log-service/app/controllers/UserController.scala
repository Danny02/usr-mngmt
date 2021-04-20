package controllers

import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents,
                               val service: ChangesService,
                               implicit val es: ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action.async { implicit request: Request[AnyContent] =>
    service.changesOverview().map(c => Ok(views.html.index(c)))
  }

  def changes(id: String) = Action.async { implicit request: Request[AnyContent] =>
    service.changes(id).map(c => Ok(views.html.changes(c)))
  }
}
