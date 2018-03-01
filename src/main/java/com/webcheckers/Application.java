package com.webcheckers;

import java.io.InputStream;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.ui.WebServer;
import com.webcheckers.appl.GameManager;

import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;


/**
 * The entry point for the WebCheckers web application.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class Application {
  private static final Logger LOG = Logger.getLogger(Application.class.getName());

  //
  // Application Launch method
  //

  /**
   * Entry point for the WebCheckers web application.
   *
   * <p>
   * This wires the application components together.
   * of <a href='https://en.wikipedia.org/wiki/Dependency_injection'>Dependency Injection</a>
   * </p>
   *
   * @param args
   *    Command line arguments; none expected.
   */
  public static void main(String[] args) {
    // initialize Logging
    try {
      ClassLoader classLoader = Application.class.getClassLoader();
      final InputStream logConfig = classLoader.getResourceAsStream("log.properties");
      LogManager.getLogManager().readConfiguration(logConfig);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Could not initialize log manager because: " + e.getMessage());
    }

    final Gson gson = new Gson();

    final TemplateEngine templateEngine = new FreeMarkerEngine();

    final GameManager gameManager = new GameManager();

    final WebServer webServer = new WebServer(templateEngine, gameManager, gson);

    final Application app = new Application(webServer);

    app.initialize();
  }


  //
  // Attributes
  //
  private final WebServer webServer;

  //
  // Constructor
  //
  private Application(final WebServer webServer) {
    // validation
    Objects.requireNonNull(webServer, "webServer must not be null");

    this.webServer = webServer;
  }

  //
  // Private methods
  //

  private void initialize() {
    LOG.config("WebCheckers is initializing.");

    webServer.initialize();

    LOG.config("WebCheckers initialization complete.");
  }

}