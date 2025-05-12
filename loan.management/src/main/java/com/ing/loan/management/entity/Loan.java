package com.ing.loan.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @Column(nullable = false)
    private BigDecimal loanAmount;

    @Column(nullable = false)
    private Integer numberOfInstallment;

    @Column(nullable = false)
    private LocalDate createDate;

    @Column(nullable = false)
    private Boolean isPaid;

    @Column(nullable = false)
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<LoanInstallment> installments;

}
