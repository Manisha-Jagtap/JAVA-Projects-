package com.application.BankStatement.repo;

import com.application.BankStatement.entity.Statement;
import com.application.BankStatement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    @Query(value = "SELECT * FROM public.statement where account_name=?1;", nativeQuery = true)
    List<Statement> findByName(String name);

    @Query(value = "SELECT SUM(total_amount) as Gained from (SELECT account_name as Account_Name, public.statement.type as Transaction_Type, SUM(amount) AS TOTAL_AMOUNT FROM public.statement Where public.statement.type= ?1 GROUP BY account_name, public.statement.type ORDER BY account_name) where total_amount<?2 GROUP BY Transaction_Type",nativeQuery = true)
    List<Statement> findGain(String transaction_type, Double amount);

    @Query(value = "SELECT account_name as Account_Name, public.statement.type as Transaction_Type, SUM(amount) AS TOTAL_AMOUNT FROM public.statement Where public.statement.type= ?1 GROUP BY account_name, public.statement.type ORDER BY account_name",nativeQuery = true)
    List<Statement> findByType(String transaction_type);

}
