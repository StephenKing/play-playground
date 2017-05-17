package controllers;

import java.util.List;
import javax.inject.Inject;
import models.SimCard;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.simcards.*;

public class SimCards extends Controller {


  private final play.data.FormFactory formFactory;

  @Inject
  public SimCards(play.data.FormFactory formFactory) {
    this.formFactory = formFactory;
  }

  public Result list() {
    List<SimCard> simCards = SimCard.findAll();
    return ok(list.render(simCards));
  }

  public Result newProduct() {
    Form<SimCard> productForm = formFactory.form(SimCard.class);
    return ok(details.render(productForm));
  }

  public Result details(String ean) {
    final SimCard simCard = SimCard.findByEan(ean);
    if (ean == null) {
      return notFound(String.format("SimCard %s does not exist.", ean));
    }

    Form<SimCard> filledForm = formFactory.form(SimCard.class);
    return ok(details.render(filledForm));
  }

  public Result save() {
    // flash("error", "foo");
    return TODO;
  }

}
