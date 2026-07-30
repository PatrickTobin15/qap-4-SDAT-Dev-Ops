package com.golfclub.api.controller;

import com.golfclub.api.model.Tournament;
import com.golfclub.api.service.TournamentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public ResponseEntity<Tournament> addTournament(@Valid @RequestBody Tournament tournament) {
        Tournament saved = tournamentService.addTournament(tournament);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable Long id, @Valid @RequestBody Tournament tournament) {
        return ResponseEntity.ok(tournamentService.updateTournament(id, tournament));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    //  Registration 

    @PostMapping("/{tournamentId}/register/{memberId}")
    public ResponseEntity<Tournament> registerMemberToTournament(
            @PathVariable Long tournamentId, @PathVariable Long memberId) {
        return ResponseEntity.ok(tournamentService.registerMemberToTournament(tournamentId, memberId));
    }

    //  Search endpoints 

    @GetMapping("/search/start-date")
    public ResponseEntity<List<Tournament>> searchByStartDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(tournamentService.searchByStartDate(date));
    }

    @GetMapping("/search/location")
    public ResponseEntity<List<Tournament>> searchByLocation(@RequestParam String location) {
        return ResponseEntity.ok(tournamentService.searchByLocation(location));
    }
}
