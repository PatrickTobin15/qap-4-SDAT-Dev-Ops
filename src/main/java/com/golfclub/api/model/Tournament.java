package com.golfclub.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "location")
    private String location;

    @Column(name = "entry_fee")
    private BigDecimal entryFee;

    @Column(name = "cash_prize_amount")
    private BigDecimal cashPrizeAmount;

    @ManyToMany
    @JoinTable(
            name = "tournament_members",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @JsonIgnoreProperties("tournaments")
    private Set<Member> participatingMembers = new HashSet<>();

    public Tournament() {
    }

    public Tournament(LocalDate startDate, LocalDate endDate, String location,
                       BigDecimal entryFee, BigDecimal cashPrizeAmount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.entryFee = entryFee;
        this.cashPrizeAmount = cashPrizeAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(BigDecimal entryFee) {
        this.entryFee = entryFee;
    }

    public BigDecimal getCashPrizeAmount() {
        return cashPrizeAmount;
    }

    public void setCashPrizeAmount(BigDecimal cashPrizeAmount) {
        this.cashPrizeAmount = cashPrizeAmount;
    }

    public Set<Member> getParticipatingMembers() {
        return participatingMembers;
    }

    public void setParticipatingMembers(Set<Member> participatingMembers) {
        this.participatingMembers = participatingMembers;
    }
}
