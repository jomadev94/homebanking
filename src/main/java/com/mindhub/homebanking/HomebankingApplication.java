package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

    private static final Logger logger= LoggerFactory.getLogger(HomebankingApplication.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner initData(ClientRepository clientRepo, AccountRepository accountRepo, TransactionRepository transactionRepo, LoanRepository loanRepo, ClientLoanRepository clientLoanRepo, CardRepository cardRepo) {
//        logger.info("Init Database...");
//        return (args) -> {
//            loanRepo.deleteAll();
//            transactionRepo.deleteAll();
//            cardRepo.deleteAll();
//            accountRepo.deleteAll();
//            clientRepo.deleteAll();
//            Client client1 = new Client("Melba", "Lorenzo", "melba@mindhub.com", passwordEncoder.encode("1234"), Role.CLIENT);
//            Client client2 = new Client("Jose Maria", "Pereyra", "jomapereyra@hotmail.com", passwordEncoder.encode("5678"), Role.CLIENT);
//            Client client3 = new Client("admin", "admin", "admin", passwordEncoder.encode("1111"), Role.ADMIN);
//            Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.0, client1);
//            Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.0, client1);
//            Account account3 = new Account("VIN003", LocalDateTime.now(), 520.0, client2);
//            Loan loan1 = new Loan("Hipotecario", 500000, Set.of(12, 24, 36, 48, 60));
//            Loan loan2 = new Loan("Personal", 100000, Set.of(6, 12, 24));
//            Loan loan3 = new Loan("Automotriz", 300000, Set.of(6, 12, 24, 36));
//            clientRepo.save(client1);
//            clientRepo.save(client2);
//            clientRepo.save(client3);
//            accountRepo.save(account1);
//            accountRepo.save(account2);
//            accountRepo.save(account3);
//            loanRepo.save(loan1);
//            loanRepo.save(loan2);
//            loanRepo.save(loan3);
//            clientLoanRepo.save(new ClientLoan(400000, 60, client1, loan1));
//            clientLoanRepo.save(new ClientLoan(50000, 12, client1, loan2));
//            clientLoanRepo.save(new ClientLoan(100000, 24, client2, loan2));
//            clientLoanRepo.save(new ClientLoan(200000, 36, client2, loan3));
//            transactionRepo.save(new Transaction(TransactionType.DEBIT, -3000.0, "pago de servico telefonico", account1));
//            transactionRepo.save(new Transaction(TransactionType.CREDIT, 540.0, "prestamo", account1));
//            transactionRepo.save(new Transaction(TransactionType.CREDIT, 1200.0, "netflix", account2));
//            transactionRepo.save(new Transaction(TransactionType.DEBIT, -470.0, "factura de luz", account2));
//            transactionRepo.save(new Transaction(TransactionType.CREDIT, 76000.0, "sueldo", account3));
//            transactionRepo.save(new Transaction(TransactionType.DEBIT, -5210.0, "videojuegos", account3));
//            transactionRepo.save(new Transaction(TransactionType.CREDIT, 850.20, "generico", account3));
//            transactionRepo.save(new Transaction(TransactionType.DEBIT, -1300.0, "generico", account3));
//            cardRepo.save(new Card(client1.getFirstName().toUpperCase() + " " + client1.getLastName().toUpperCase(), CardType.DEBIT, CardColor.GOLD, "3456678907321843", 428, LocalDateTime.now().plusYears(5), LocalDateTime.now(), client1));
//            cardRepo.save(new Card(client1.getFirstName().toUpperCase() + " " + client1.getLastName().toUpperCase(), CardType.DEBIT, CardColor.TITANIUM, "4597654375285822", 354, LocalDateTime.now().plusYears(5), LocalDateTime.now(), client1));
//            cardRepo.save(new Card(client2.getFirstName().toUpperCase() + " " + client2.getLastName().toUpperCase(), CardType.CREDIT, CardColor.SILVER, "4750232217443716", 221, LocalDateTime.now().plusYears(5), LocalDateTime.now(), client2));
//        };
//    }

}
