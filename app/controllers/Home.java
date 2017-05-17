package controllers;

import play.mvc.*;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Home extends Controller {

  public Result index() {
    return ok("It works!");
  }

  public Result echo(String text) {
    return ok(views.html.index.render(text));
  }

  public Result random() {
    Date date = new Date();
    return ok(views.html.index.render(date.toString()));
  }

  public CompletionStage<Result> async() {
    return CompletableFuture.supplyAsync(() -> getFromRemote())
        .thenApply(i -> ok("Got result: " + i));
  }

  public String getFromRemote() {
    return (new Date()).toString();
  }
}
