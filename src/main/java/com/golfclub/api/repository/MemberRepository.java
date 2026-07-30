package com.golfclub.api.repository;

import com.golfclub.api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByMemberNameContainingIgnoreCase(String name);

    List<Member> findByMembershipTypeIgnoreCase(String membershipType);

    List<Member> findByMemberPhoneNumberContaining(String phoneNumber);

    @Query("SELECT DISTINCT m FROM Member m JOIN m.tournaments t WHERE t.startDate = :startDate")
    List<Member> findByTournamentStartDate(@Param("startDate") LocalDate startDate);
}
