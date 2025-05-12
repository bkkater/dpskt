package br.edu.iff.ccc.bsi.dpskt_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.iff.ccc.bsi.dpskt_api.entities.Clock;
import br.edu.iff.ccc.bsi.dpskt_api.repository.ClockRepository;

@Service
public class ClockService {

    @Autowired
    private ClockRepository clockRepository;

    public List<Clock> findAllClocks() {
        return clockRepository.findAll();
    }

    public List<Clock> findClockByUser(String discordId) {
        return clockRepository.findByUser_DiscordId(discordId);
    }

    public Optional<Clock> findLastPendingClock(String discordId) {
        return clockRepository.findByUser_DiscordId(discordId).stream()
                .filter(clock -> clock.getEndAt() == null)
                .findFirst();
    }

    public boolean hasPendingClock(String discordId) {
        return clockRepository.findByUser_DiscordId(discordId).stream().anyMatch(clock -> clock.getEndAt() == null);
    }

    public Clock createClock(Clock clockModel) {
        return clockRepository.save(clockModel);
    }

    public boolean clockExists(UUID id) {
        return clockRepository.findById(id) != null;
    }

    public Clock patchClock(UUID id, LocalDateTime endAt) {
        return clockRepository.findById(id)
                .map(clock -> {
                    if (endAt != null)
                        clock.setEndAt(endAt);
                    return clockRepository.save(clock);
                })
                .orElse(null);
    }

    public Optional<Clock> deleteClock(UUID id) {
        Optional<Clock> clock = clockRepository.findById(id);

        if (!clock.isPresent()) {
            return Optional.empty();
        }

        clockRepository.deleteById(id);

        return clock;
    }

}
