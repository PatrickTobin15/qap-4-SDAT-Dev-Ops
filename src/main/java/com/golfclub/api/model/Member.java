package com.golfclub.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Member name is required")
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_address")
    private String memberAddress;

    @Email(message = "Email must be valid")
    @Column(name = "member_email_address")
    private String memberEmailAddress;

    @Column(name = "member_phone_number")
    private String memberPhoneNumber;

    @Column(name = "membership_start_date")
    private LocalDate membershipStartDate;

    // Annual, Monthly, Lifetime
    @Column(name = "membership_type")
    private String membershipType;

    @ManyToMany(mappedBy = "participatingMembers")
    @JsonIgnoreProperties("participatingMembers")
    private Set<Tournament> tournaments = new HashSet<>();

    public Member() {
    }

    public Member(String memberName, String memberAddress, String memberEmailAddress,
                  String memberPhoneNumber, LocalDate membershipStartDate, String membershipType) {
        this.memberName = memberName;
        this.memberAddress = memberAddress;
        this.memberEmailAddress = memberEmailAddress;
        this.memberPhoneNumber = memberPhoneNumber;
        this.membershipStartDate = membershipStartDate;
        this.membershipType = membershipType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getMemberEmailAddress() {
        return memberEmailAddress;
    }

    public void setMemberEmailAddress(String memberEmailAddress) {
        this.memberEmailAddress = memberEmailAddress;
    }

    public String getMemberPhoneNumber() {
        return memberPhoneNumber;
    }

    public void setMemberPhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }

    public LocalDate getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public Set<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(Set<Tournament> tournaments) {
        this.tournaments = tournaments;
    }
}
