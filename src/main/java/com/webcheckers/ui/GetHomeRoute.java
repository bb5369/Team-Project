package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetHomeRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameManager gameManager) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(playerLobby, "gameManager must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
        LOG.config("GetHomeRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetHomeRoute is invoked.");

        Map<String, Object> vm = new HashMap<>();

        vm.put("title", "Welcome!");
        Player currentPlayer = request.session().attribute("Player");

        if ( ! playerLobby.isPlayerInLobby(currentPlayer)) {
            // This means that the lobby was cleared so we must log the user out
            // TODO: make this a part of player sign-out story.
            request.session().removeAttribute("Player");

            currentPlayer = null;
        }

        if (currentPlayer != null) {
            vm.put("currentPlayer", currentPlayer);
            vm.put("activePlayers", playerLobby.getActivePlayers());
            vm.put("gameRoute", WebServer.GAME_URL);
            vm.put("hasGames", gameManager.getGameList().size() > 0);
            vm.put("activeGames", gameManager.getGameList());
            vm.put("spectatorRoute", WebServer.SPECTATE_URL);

            if(!gameManager.isPlayerASpectator(currentPlayer) && gameManager.isPlayerInAGame(currentPlayer) && !gameManager.getGame(currentPlayer).isResigned()){
                if(gameManager.isPlayerInAGame(currentPlayer) && gameManager.getGame(currentPlayer).isWon())
                    gameManager.destoryGame(gameManager.getGame(currentPlayer));
                response.redirect(WebServer.GAME_URL);
            }

        } else {
            vm.put("activePlayerCount", this.playerLobby.getActivePlayerCount());
            vm.put("signInUrl", WebServer.SIGNIN_URL);
        }

        if (request.session().attribute("message") != null) {
            final Message messageToRender = request.session().attribute("message");
            request.session().removeAttribute("message");
            vm.put("message", messageToRender);
        }

        return templateEngine.render(new ModelAndView(vm , "home.ftl"));
    }

}