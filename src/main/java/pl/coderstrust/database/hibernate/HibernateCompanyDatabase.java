package pl.coderstrust.database.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.WithNameIdIssueDate;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;

@Transactional
@Service
public class HibernateCompanyDatabase<T extends WithNameIdIssueDate> implements Database<T>,
    Serializable {

  private final Logger logger = LoggerFactory.getLogger(HibernateCompanyDatabase.class);

  public HibernateCompanyDatabase() {
  }

  @Autowired
  CompanyRepository companyRepository;

  @Override
  public synchronized long addEntry(T entry) {
    Company savedCompany = (Company) companyRepository.save(entry);
    return savedCompany.getId();
  }

  @Override
  public void deleteEntry(long id) {
    if (!idExist(id)) {
      logger.warn(" from deleteEntry (hibernateDatabase): "
          + ExceptionMsg.INVOICE_NOT_EXIST);
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    } else {
      companyRepository.delete(id);
    }
  }

  @Override
  public T getEntryById(long id) {
    if (!idExist(id)) {
      logger.warn(" from getEntryByiD (hibernateDatabase): "
          + ExceptionMsg.INVOICE_NOT_EXIST);
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    } else {
      return (T) companyRepository.findOne(id);
    }
  }

  @Override
  public void updateEntry(T entry) {
    companyRepository.save(entry);
  }

  @Override
  public List<T> getEntries() {
    return (List<T>) companyRepository.findAll();
  }

  @Override
  public boolean idExist(long id) {
    return companyRepository.exists(id);
  }
}