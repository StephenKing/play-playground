import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import controllers.routes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.SimCard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import play.twirl.api.Content;

public class ApplicationTest extends WithApplication {

  @Override
  protected Application provideApplication() {
    return new GuiceApplicationBuilder().build();
  }

  @Before
  public void setUp() throws Exception {
    Http.Context ctx = mock(Http.Context.class);
    Http.Flash flash = mock(Http.Flash.class);
    when(ctx.flash()).thenReturn(flash);
    Http.Context.current.set(ctx);
  }

  @After
  public void tearDown() throws Exception {
    Http.Context.current.set(null);
  }

  @Test
  public void renderListTemplate() {
    List<SimCard> list = new ArrayList<>();
    list.add(new SimCard("123", "456", "789"));
    Content html = views.html.simcards.list.render(list);

    assertEquals("text/html", html.contentType());
    assertTrue(Helpers.contentAsString(html).contains("All SimCards"));
    assertTrue(Helpers.contentAsString(html).contains(">123<"));
  }

  @Test
  public void testListAction() {
    Helpers.running(Helpers.fakeApplication(), () -> {
      Result result = Helpers.route(Helpers.fakeRequest(controllers.routes.SimCards.list()));
      assertEquals(Status.OK, result.status());
      // testing for the first SIM card defined
      assertTrue(Helpers.contentAsString(result).contains("1234567"));
    });
  }

  @Test
  public void newActionSuccessfull() {
    Map<String, String> formData = new HashMap<String, String>();
    formData.put("imsi", "imsi123");
    formData.put("msisdn", "msisdn123");
    formData.put("owner", "owner123");

    Helpers.running(Helpers.fakeApplication(), () -> {
      Result result = Helpers.route(
          Helpers.fakeRequest(Helpers.POST, controllers.routes.SimCards.save().url())
              .bodyForm(formData));
      assertEquals(Status.SEE_OTHER, result.status());
    });
  }

  @Test
  public void newActionValidationWithEmptyBody() {
    Helpers.running(Helpers.fakeApplication(), () -> {
      Result result = Helpers
          .route(Helpers.fakeRequest(Helpers.POST, controllers.routes.SimCards.save().url()));
      assertEquals(Status.BAD_REQUEST, result.status());
    });
  }

  @Test
  public void newActionValidationWithEmptyField() {
    Map<String, String> formData = new HashMap<String, String>();
    formData.put("imsi", "imsi123");
    formData.put("msisdn", ""); // empty
    formData.put("owner", "owner123");

    Helpers.running(Helpers.fakeApplication(), () -> {
      Result result = Helpers.route(
          Helpers.fakeRequest(Helpers.POST, controllers.routes.SimCards.save().url())
              .bodyForm(formData));
      assertEquals(Status.BAD_REQUEST, result.status());
    });
  }

}
