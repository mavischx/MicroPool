package com.example.matchservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface MatchRepository extends JpaRepository<Match, Long> {
}
