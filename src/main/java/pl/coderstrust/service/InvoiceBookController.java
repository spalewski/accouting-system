package pl.coderstrust.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;

import java.util.List;

@RestController
public class InvoiceBookController {

  private InvoiceBook ib = new InvoiceBook();

  @RequestMapping("bla")
  public String show() {
    return "dupa";
  }

  @RequestMapping(value = "addInvoice", method = RequestMethod.POST)
  public void addInvoice(@RequestBody Invoice invoice) {
    ib.addInvoice(invoice);
    System.out.println("tralala fakturka dodana");
  }

  @RequestMapping("getInvoice/{visibleId}")
  public Invoice getInvoiceById(@PathVariable("visibleId") String visibleId) {
    return ib.findInvoice(visibleId);
  }
  @RequestMapping("getInvoice")
  public List<Invoice> getInvoice() {
    return ib.getInvoices();
  }

  @RequestMapping("removeInvoice/{visibleId}")
  public void remoceInvoice(@PathVariable("visibleId") String visibleId) {
    ib.removeInvoice(visibleId);
  }

}
