package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller, Request, Result}
import play.api.libs.json._
import play.api.cache._


class User @Inject() (cache: CacheApi) extends Controller {
  def AuthenticatedAction(f: Request[AnyContent] => Result): Action[AnyContent] = {
    Action { request =>
      (request.session.get("id").flatMap { id =>
        cache.get[JsValue](id + "profile")
      } map { profile =>
        f(request)
      }).orElse {
        Some(Redirect(routes.Application.index()))
      }.get
    }
  }
  
  def index: Action[AnyContent] = AuthenticatedAction { request =>
    val id = request.session.get("id").get
    val profile = cache.get[JsValue](id + "profile").get
    Ok(views.html.user(profile))
  }
}