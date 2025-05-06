package com.group9.LoanCalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private KafkaProducerService kafkaProducerService;  // เพิ่ม KafkaProducerService

    // POST สำหรับการคำนวณดอกเบี้ย
    @PostMapping("/calculate")
    public Double calculateLoanInterest(@RequestBody LoanEntity loan) {
        Double totalAmount = loanService.calculateLoanInterest(loan);
        // ส่งข้อความไปยัง Kafka เมื่อคำนวณดอกเบี้ยเสร็จ
        String message = "Loan interest calculated for customer: " + loan.getCustomerName() + ", Total Amount: " + totalAmount;
        kafkaProducerService.sendMessage("loan-calculation-topic", message);  // ส่งข้อความไปยัง Kafka
        return totalAmount;
    }

    // POST สำหรับบันทึกข้อมูลลูกค้าและเงินกู้ พร้อมกับรหัสลูกค้าและรหัสพนักงานที่อนุมัติ
    @PostMapping("/add")
    public LoanEntity addLoan(@RequestBody LoanEntity loan) {
        // บันทึกข้อมูลลูกค้าและเงินกู้
        LoanEntity savedLoan = loanService.saveLoan(loan);

        // ส่งข้อความไปยัง Kafka เมื่อบันทึกข้อมูลสำเร็จ
        String message = "New loan added for customer: " + loan.getCustomerName() + ", Loan Amount: " + loan.getLoanAmount();
        kafkaProducerService.sendMessage("loan-addition-topic", message);  // ส่งข้อความไปยัง Kafka

        return savedLoan;
    }

    // GET สำหรับค้นหาข้อมูลเงินกู้ตามรหัสลูกค้า
    @GetMapping("/customer/{customerId}")
    public LoanEntity getLoanByCustomerId(@PathVariable Long customerId) {
        return loanService.getLoanByCustomerId(customerId);
    }

    // GET สำหรับค้นหาข้อมูลเงินกู้ตามรหัสพนักงานที่อนุมัติ
    @GetMapping("/approver/{approverId}")
    public List<LoanEntity> getLoansByApproverId(@PathVariable Long approverId) {
        return loanService.getLoansByApproverId(approverId);
    }
}
