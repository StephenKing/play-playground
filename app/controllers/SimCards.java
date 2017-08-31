package controllers;

import java.util.Date;
import java.util.List;
import javax.inject.Inject;

import kamon.Kamon;
import kamon.jaeger.Jaeger;
import kamon.metric.Counter;
import kamon.prometheus.PrometheusReporter;
import kamon.trace.Span;
import models.SimCard;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.simcards.*;

public class SimCards extends Controller {


  private final play.data.FormFactory formFactory;

  @Inject
  public SimCards(play.data.FormFactory formFactory) {
    this.formFactory = formFactory;
    PrometheusReporter prom = new PrometheusReporter();
    Kamon.addReporter(prom);
    Counter c = Kamon.counter("test");
    c.increment(42L);

    Jaeger j = new Jaeger();
    Kamon.addReporter(j);
  }

  public Result list() {

    Counter req = Kamon.counter("requests.list");
    req.increment();

    List<SimCard> simCards = SimCard.findAll();


    Span listSpan = Kamon.buildSpan("list").withSpanTag("method", "get").start();
    Html res = list.render(simCards);
    listSpan.finish();

    Span takeTime = Kamon.buildSpan("take-some-time").asChildOf(listSpan).withTag("foo", "bar").start();
    takeSomeTime();
    takeTime.finish();

    return ok(res);
  }

  private void takeSomeTime() {
    long random = System.currentTimeMillis() % 3;
    if (random == 1) {
      try {
        Thread.sleep(1000 * random);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public Result newSim() {
    Form<SimCard> simcardForm = formFactory.form(SimCard.class);
    return ok(details.render(simcardForm));
  }

  public Result details(String imsi) {
    final SimCard simcard = SimCard.findByImsi(imsi);
    if (imsi == null) {
      return notFound(String.format("SimCard %s does not exist.", imsi));
    }

    Form<SimCard> filledForm = formFactory.form(SimCard.class);
    return ok(details.render(filledForm.fill(simcard)));
  }

  public Result save() {
    Logger.info("Binding request data");
    Form<SimCard> boundForm = formFactory.form(SimCard.class).bindFromRequest();
    if (boundForm.hasErrors()) {
      flash("error", "Please fill all fields!");
      return badRequest(details.render(boundForm));
    }
    Logger.info("No errors in Form");
    SimCard simcard = boundForm.get();
    Logger.info("Got SIM: {}", simcard);
    simcard.save();
    Logger.info("Saved");
    flash("success", String.format("Successfully added SIM %s", simcard));
    return redirect(routes.SimCards.list());
  }

  public Result delete(String imsi) {
    final SimCard simcard = SimCard.findByImsi(imsi);
    if (simcard == null) {
      return notFound(String.format("SIM card %s does not exist", imsi));
    }
    SimCard.remove(simcard);
    return redirect(routes.SimCards.list());
  }

}
