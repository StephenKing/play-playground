package controllers;

import play.mvc.*;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Home extends Controller {

  public Result index() {
    return redirect(routes.SimCards.list());
  }
}
