package integrations

import java.net.URI
import org.scalatest._
import org.fluentlenium.core.domain.FluentWebElement
import play.api.test.TestBrowser
import test.ConfiguredTestSuite

import scala.collection.JavaConverters._

@DoNotDiscover class SectionTemplateTest extends FlatSpec with Matchers with ConfiguredTestSuite {

  "Tag" should "render front title" in goTo("/uk-news") { browser =>
    browser.el("[data-test-id=header-title]").text should be ("UK news")
  }

  it should "add alternate pages to editionalised sections for /uk/culture" in goTo("/uk/culture") { browser =>

    val alternateLinks = getAlternateLinks(browser)
    alternateLinks.size should be (2)
    alternateLinks.exists(link => toPath(link.attribute("href")) == "/us/culture" && link.attribute("hreflang") == "en-US") should be (true)
    alternateLinks.exists(link => toPath(link.attribute("href")) == "/au/culture" && link.attribute("hreflang") == "en-AU") should be (true)

  }

  def getAlternateLinks(browser: TestBrowser): Seq[FluentWebElement] = {
    import browser._
    $("link[rel='alternate']").asScala.toList
      .filterNot(_.attribute("type") == "application/rss+xml")
      .filter(element => {
        val href: Option[String] = Option(element.attribute("href"))
        href.isDefined && !href.exists(_.contains("ios-app"))
      })
  }

  it should "add alternate pages to editionalised sections for /au/culture" in goTo("/au/culture") { browser =>

    val alternateLinks = getAlternateLinks(browser)
    alternateLinks.size should be (2)
    alternateLinks.exists(link => toPath(link.attribute("href")) == "/us/culture" && link.attribute("hreflang") == "en-US") should be (true)
    alternateLinks.exists(link => toPath(link.attribute("href")) == "/uk/culture" && link.attribute("hreflang") == "en-GB") should be (true)
  }

  it should "not add alternate pages to non editionalised sections" in goTo("/books") { browser =>

    val alternateLinks = getAlternateLinks(browser)
    alternateLinks should be (empty)
  }

  it should "not add alternate pages to 'all' pages for a section" in goTo("/business/1929/oct/24/all") { browser =>

    val alternateLinks = getAlternateLinks(browser)
    alternateLinks should be (empty)
  }

  private def toPath(url: String) = new URI(url).getPath
}