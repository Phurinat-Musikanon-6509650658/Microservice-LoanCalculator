package com.group9.LoanCalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    // คำนวณดอกเบี้ยและยอดชำระ
    public Double calculateLoanInterest(LoanEntity loan) {
        double principal = loan.getLoanAmount();
        double annualInterestRate = loan.getInterestRate();
        int termInYears = loan.getLoanTerm() / 12;  // คำนวณจากเดือนเป็นปี

        // สูตรคำนวณดอกเบี้ยแบบ Simple Interest
        double interest = principal * annualInterestRate * termInYears;
        double totalAmount = principal + interest;

        return totalAmount;
    }

    // บันทึกข้อมูลลูกค้าและยอดเงินกู้
    public LoanEntity saveLoan(LoanEntity loan) {
        // เช็คว่ามีข้อมูลลูกค้าและผู้อนุมัติหรือไม่
        if (loan.getCustomerId() == null || loan.getApproverId() == null) {
            throw new IllegalArgumentException("Customer ID and Approver ID must be provided");
        }
        return loanRepository.save(loan);
    }

    // ค้นหาข้อมูลเงินกู้ตามรหัสลูกค้า
    public LoanEntity getLoanByCustomerId(Long customerId) {
        return loanRepository.findByCustomerId(customerId);
    }

    // ค้นหาข้อมูลเงินกู้ตามรหัสพนักงานที่อนุมัติ
    public List<LoanEntity> getLoansByApproverId(Long approverId) {
        return loanRepository.findByApproverId(approverId);
    }
}
