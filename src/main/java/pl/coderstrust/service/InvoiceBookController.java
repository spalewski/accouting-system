package pl.coderstrust.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;

@RestController
@RequestMapping("invoice")
@Configuration
public class InvoiceBookController extends BookController<Invoice> {

  public InvoiceBookController(InvoiceBook invoiceBook) {
    super.book = invoiceBook;
  }
}
