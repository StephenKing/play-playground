package controllers;

import java.util.List;
import javax.inject.Inject;
import models.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.products.*;

public class Products extends Controller {


  private final play.data.FormFactory formFactory;

  @Inject
  public Products(play.data.FormFactory formFactory) {
    this.formFactory = formFactory;
  }

  public Result list() {
    List<Product> products = Product.findAll();
    return ok(views.html.products.list.render(products));
  }

  public Result newProduct() {
    Form<Product> productForm = formFactory.form(Product.class);
    return ok(details.render(productForm));
  }

  public Result details(String ean) {
    final Product product = Product.findByEan(ean);
    if (ean == null) {
      return notFound(String.format("Product %s does not exist.", ean));
    }

    Form<Product> filledForm = formFactory.form(Product.class);
    return ok(details.render(filledForm));
  }

  public Result save() {
    // flash("error", "foo");
    return TODO;
  }

}
