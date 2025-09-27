package com.application.BankStatement.repo;

import com.application.BankStatement.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatementRepo extends JpaRepository<Statement,Integer> {
}
