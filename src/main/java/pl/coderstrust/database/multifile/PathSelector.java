package pl.coderstrust.database.multifile;

import pl.coderstrust.model.Invoice;

import java.io.File;

public class PathSelector {

  private StringBuilder stringBuilder = new StringBuilder();
  private String filePath = "";
  private Configuration dbConfig;

  public PathSelector() {
    dbConfig = new Configuration();
  }

  public String getFilePath(Invoice invoice) {
    String invoiceDateYear = String.valueOf(invoice.getIssueDate().getYear());
    String invoiceDateMonth = String.valueOf(invoice.getIssueDate().getMonth());
    String invoiceDateDay = String.valueOf(invoice.getIssueDate().getDayOfMonth());
    stringBuilder.append(dbConfig.getJsonFilePath());
    stringBuilder.append(invoiceDateYear);
    stringBuilder.append(File.separator);
    stringBuilder.append(invoiceDateMonth);
    stringBuilder.append(File.separator);
    stringBuilder.append(invoiceDateDay);
    stringBuilder.append(".json");

    filePath = stringBuilder.toString();
    return filePath;
  }
}
