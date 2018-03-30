package pl.coderstrust.e2e.validInputTest;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.TestsConfiguration;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.testHelpers.ObjectMapperHelper;
import pl.coderstrust.e2e.testHelpers.TestCasesGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class AbstractValidInputTests {

  protected TestsConfiguration config = new TestsConfiguration();
  protected TestCasesGenerator generator = new TestCasesGenerator();
  protected ObjectMapperHelper mapper = new ObjectMapperHelper();
  protected LocalDate currentDate = LocalDate.now();
  protected Invoice testInvoice;
  protected ArrayList<Invoice> testInvoices = new ArrayList<>();
  protected Pattern extractIntFromString = Pattern.compile(config.getIntFromStringRegexPattern());


  @Test
  public void shouldReturnCorrectStatusCodeWhenServiceIsUp() {
    given()
        .when()
        .get(getBasePath())

        .then()
        .statusCode(config.getServerOkStatusCode());
  }

  protected abstract String getBasePath();

  @Test
  public void shouldCorrectlyAddAndGetInvoiceById() {
    long invoiceId = addInvoice(testInvoice);
    testInvoice.setId(invoiceId);
    given()
        .when()
        .get(getBasePathWithInvoiceId(invoiceId)).

       then()
        .assertThat()
        .body(jsonEquals(mapper.toJson(testInvoice)));

  }

  protected abstract String getBasePathWithInvoiceId(long invoiceId);


  protected abstract long addInvoice(Invoice testInvoice);

  protected long getInvoiceIdFromServiceResponse(String response) {
    Matcher matcher = extractIntFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }

  @Test
  public void shouldCorrectlyUpdateInvoice() {
    long invoiceId = addInvoice(testInvoice);
    Invoice updatedInvoice = generator.getTestInvoice(
        config.getDefaultTestInvoiceNumber() + 1, config.getDefaultEntriesCount());
    updatedInvoice.setId(invoiceId);
    updatedInvoice.setBuyer(testInvoice.getBuyer());
    updatedInvoice.setSeller(testInvoice.getSeller());
    given()
        .contentType("application/json")
        .body(updatedInvoice)
        .when()
        .put(getBasePathWithInvoiceId(invoiceId));

    given()
        .when()
        .get(getBasePathWithInvoiceId(invoiceId))

        .then()
        .assertThat()
        .body(jsonEquals(mapper.toJson(updatedInvoice)));
  }

  @Test
  public void shouldCorrectlyDeleteInvoiceById() {
    long invoiceId = addInvoice(testInvoice);
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .delete(getBasePathWithInvoiceId(invoiceId));

    given()
        .when()
        .get(getBasePathWithInvoiceId(invoiceId))

        .then()
        .assertThat()
        .body(equalTo(""));
  }

  @Test
  public void shouldAddSeveralInvoicesAndReturnCorrectMessage() {
    for (int i=0;i<config.getTestInvoicesCount();i++) {
      given()
          .contentType("application/json")
          .body(testInvoice)

          .when()
          .post(getBasePath())

          .then()
          .assertThat()
          .body(containsString("Entry added under id :"));
    }
  }

  @Test(dataProvider = "validDates")
  public void shouldAddSeveralInvoicesAndFindThemByIssueDate(LocalDate newDate) {
    int invoicesAtDateCount = getInvoicesCountForDateRange(newDate, newDate);
    testInvoice.setIssueDate(newDate);
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .post(getBasePath());

    int invoicesAdded = getInvoicesCountForDateRange(newDate, newDate) - invoicesAtDateCount;
    Assert.assertEquals(invoicesAdded, 1);
  }

  @DataProvider(name = "validDates")
  public Object[] validDatesProvider() {
    Object[] validDates = new Object[10];
    for (int i = 0; i < config.getTestInvoicesCount(); i++) {
      validDates[i] = LocalDate.now().plusYears(i);
    }
    return validDates;
  }

  protected int getInvoicesCountForDateRange(LocalDate dateFrom, LocalDate dateTo) {
    String path = getBasePathWithDateRange(dateFrom, dateTo);
    String response = given()
        .get(path)
        .body().print();
    return mapper.toInvoiceList(response).size();
  }

  protected abstract String getBasePathWithDateRange(LocalDate dateFrom, LocalDate dateTo);
}


