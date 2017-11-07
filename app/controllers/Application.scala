package controllers

import play.api.mvc._

//import play.api.libs.iteratee._
//
//import play.api.libs.concurrent.Execution.Implicits.defaultContext
//import play.api.libs.EventSource
//import play.api.libs.ws.WS
//import play.api.libs.oauth.{RequestToken, ConsumerKey, OAuthCalculator}
//import play.api.Play.current

class Application extends Controller {

//  val query = "DSDDirceu"
//
//  lazy val tweets: Enumerator[String] = {
//
//    Concurrent.unicast[String](onStart = channel => {
//      val consumerKey = ConsumerKey("s7Wf2E3uAq5gbQdWl7PMHDho9", "kFgD2fjdwL51FqUQMHhelwnx8ndjadjG4SJQOR0FlBMtzUfEad")
//      val accessToken = RequestToken("86628543-wuxBJVBMLFThiBFGSka4VAqVPdUUVFRjDt808sPnO", "stdNfRbzEB1SoB39ze24s9mr7u6pTXORE85q8Lkh0CpBu")
//
//      def twitterIteratee = Iteratee.foreach[Array[Byte]] {
//        tweet =>
//          val msg = new String(tweet, "UTF-8")
//          println(msg)
//          channel.push(msg.replaceAll("'", ""))
//      }
//
//      WS.url("https://stream.twitter.com/1.1/statuses/filter.json?track=" + query)
//        .sign(OAuthCalculator(consumerKey, accessToken))
//        .get(headers => twitterIteratee)
//    })
//  }
//
//  def index = Action {
//    Ok(views.html.index())
//  }
//
//  def liveTweet = Action {
//    Ok.chunked(tweets &> EventSource()).as("text/event-stream")
//  }

}