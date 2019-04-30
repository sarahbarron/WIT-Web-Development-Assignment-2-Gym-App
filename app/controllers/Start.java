package controllers;

import play.Logger;
import play.mvc.Controller;

// home.html page is rendered, when you first visit the website
public class Start extends Controller {
  public static void index() {
    Logger.info("Rendering Home Page");
    render("home.html");
  }

  //  about.html page is rendered, when about is clicked on the nav bar
  public static void about() {
    Logger.info("Rendering About Page");
    render("about.html");
  }
}
