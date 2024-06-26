package com.matheus.emprestimo.app.service;

import com.matheus.emprestimo.app.dto.CustomerLoanRequest;
import com.matheus.emprestimo.app.dto.CustomerLoanResponse;
import com.matheus.emprestimo.app.dto.LoanResponse;
import com.matheus.emprestimo.domain.Loan;
import com.matheus.emprestimo.domain.LoanType;
import com.matheus.emprestimo.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private final CustomerRepository customerRepository;

    public LoanService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerLoanResponse checkLoanAvailability(CustomerLoanRequest loanRequest) {

        var customer = loanRequest.toCustomer();
        var loan = new Loan(customer);

        List<LoanResponse> loans = new ArrayList<>();

        if (loan.isPersonalLoanAvailable()) {
            loans.add(new LoanResponse(LoanType.PERSONAL, loan.getPersonalLoanInterestRate()));
        }

        if (loan.isConsigmentLoanAvailable()) {
            loans.add(new LoanResponse(LoanType.CONSIGNMENT, loan.getConsigmentLoanInterestRate()));
        }

        if (loan.isGuaranteedLoanAvailable()) {
            loans.add(new LoanResponse(LoanType.GUARANTEED, loan.getGuaranteedLoanInterestRate()));
        }

        customerRepository.save(customer);

        return new CustomerLoanResponse(loanRequest.name(), loans);

    }
}
