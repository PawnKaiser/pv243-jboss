package cz.muni.fi.pv243.et.service.impl;

import cz.muni.fi.pv243.et.data.ExpenseReportListProducer;
import cz.muni.fi.pv243.et.data.ExpenseReportRepository;
import cz.muni.fi.pv243.et.model.ExpenseReport;
import cz.muni.fi.pv243.et.model.Person;
import cz.muni.fi.pv243.et.model.ReportStatus;
import cz.muni.fi.pv243.et.service.ExpenseReportService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;

@Stateless
public class ExpenseReportServiceImpl implements ExpenseReportService {

    @Inject
    private ExpenseReportRepository repository;

    @Inject
    private ExpenseReportListProducer listProducer;

    @Override
    public void save(ExpenseReport report) {
        if (report == null) {
            throw new IllegalArgumentException();
        }

        if (report.getId() == null) {
            repository.create(report);
        } else {
            repository.update(report);
        }
    }

    @Override
    public void remove(ExpenseReport report) {
        if (report == null) {
            throw new IllegalArgumentException();
        }

        repository.remove(report);
    }

    @Override
    public void claim(ExpenseReport report, Person verifier) {
        if (report == null || verifier == null) {
            throw new IllegalArgumentException();
        }

        if (report.getVerifier() != null) {
            throw new RuntimeException("report already has a verifier");
        }

        if (report.getStatus() == ReportStatus.APPROVED ||
                report.getStatus() == ReportStatus.REJECTED ||
                report.getStatus() == ReportStatus.SETTLED) {
            throw new RuntimeException("report status indicates that it should have already had a verifier assigned");
        }

        report.setVerifier(verifier);

        this.save(report);
    }

    @Override
    public ExpenseReport get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        return listProducer.get(id);
    }

    @Override
    public Collection<ExpenseReport> findAll() {
        return listProducer.getAll();
    }

    @Override
    public Collection<ExpenseReport> findForSubmitter(Person submitter) {
        if (submitter == null) {
            throw new IllegalArgumentException();
        }

        return listProducer.getAllForSubmitter(submitter);
    }

    @Override
    public Collection<ExpenseReport> findForVerifier(Person verifier) {
        if (verifier == null) {
            throw new IllegalArgumentException();
        }

        return listProducer.getAllForVerifier(verifier);
    }

    @Override
    public Collection<ExpenseReport> findWithNoVerifierAssigned() {
        return listProducer.getAllWithNoVerifierAssigned();
    }

    @Override
    public Collection<ExpenseReport> findByStatus(ReportStatus status) {
        if (status == null) {
            throw new IllegalArgumentException();
        }

        return listProducer.getAllBy(status);
    }

    @Override
    public Collection<ExpenseReport> findForSubmitterWithStatus(Person submitter, ReportStatus status) {
        if (submitter == null || status == null) {
            throw new IllegalArgumentException();
        }

        return listProducer.getAllForSubmitterWithStatus(submitter, status);
    }

    @Override
    public Collection<ExpenseReport> findForVerifierWithStatus(Person verifier, ReportStatus status) {
        if (verifier == null || status == null) {
            throw new IllegalArgumentException();
        }

        return listProducer.getAllForVerifierWithStatus(verifier, status);
    }
}
