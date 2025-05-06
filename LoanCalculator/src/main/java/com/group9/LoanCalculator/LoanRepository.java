package com.group9.LoanCalculator;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    LoanEntity findByCustomerId(Long customerId);  // ค้นหาตามรหัสลูกค้า
    List<LoanEntity> findByApproverId(Long approverId);  // ค้นหาตามรหัสพนักงานที่อนุมัติ
}
