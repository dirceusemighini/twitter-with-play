package controllers

import play.api.mvc._

import play.api.libs.iteratee._

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.EventSource
import play.api.libs.ws.WS
import play.api.libs.oauth.{RequestToken, ConsumerKey, OAuthCalculator}

object Application extends Controller {

  val query = "partiu"

  lazy val tweets: Enumerator[String] = {
    Concurrent.unicast[String](onStart = channel => {
      val consumerKey = ConsumerKey("aiuO34OBqnrNhpAYzRlQ", "lUkhaS3We3HAH8pGtldC7fNEHnfZU6eWiWgTWKZ8ObQ")
      val accessToken = RequestToken("86628543-35lewf8PWmE2QTpHvYhpjBITTbMUZZfQSDRGzfgss", "evnf4cDO69YnMfqEqvT7bKld9RjWJUBPCevh9bXx6O3L0")

      def twitterIteratee = Iteratee.foreach[Array[Byte]] {
        tweet =>
          val msg = new String(tweet, "UTF-8")
          channel.push(msg.replaceAll("'", ""))
      }

      WS.url("https://stream.twitter.com/1.1/statuses/filter.json?track=" + query)
        .sign(OAuthCalculator(consumerKey, accessToken))
        .get(headers => twitterIteratee)
    })
  }

  def index = Action {
    Ok(views.html.index())
  }

  def liveTweet = Action {
    Ok.chunked(tweets &> EventSource()).as("text/event-stream")
  }

}