package controllers

import play.api._
import twitter4j.{QueryResult, Query, TwitterFactory}
import play.mvc.Controller

/**
 * Created by dirceu on 3/24/14.
 */
class WebSocketController extends Controller {
  //The factory instance is re-useable and thread safe.
    val twitter = TwitterFactory.getSingleton();
    val query = new Query("source:twitter4j yusukey");
    val result = twitter.search(query);


}
