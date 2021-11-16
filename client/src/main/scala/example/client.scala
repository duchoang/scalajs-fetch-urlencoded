package example

import scala.scalajs.js

import org.scalajs.dom._

object example {

  def main(args: Array[String]): Unit = {

    val button1 = document.getElementById("button1")
    if (button1 != null) {
      button1.addEventListener(
        "click",
        (_: Event) => {
          val headers = new Headers()
          headers.set("content-type", "application/x-www-form-urlencoded")
          Fetch
          .fetch(
            "/test-api",
            new RequestInit {
              method = HttpMethod.POST
              body = "key1=value1&key2=value2"
              headers = headers
            }
          )
        }
      )
    }

    val button2 = document.getElementById("button2")
    if (button2 != null) {
      button2.addEventListener(
        "click",
        (_: Event) => {
          Fetch
          .fetch(
            "/test-api",
            new RequestInit {
              method = HttpMethod.POST
              body = "key1=value1&key2=value2"
              headers = js.Dictionary("content-type" -> "application/x-www-form-urlencoded")
            }
          )
        }
      )
    }
  }
}