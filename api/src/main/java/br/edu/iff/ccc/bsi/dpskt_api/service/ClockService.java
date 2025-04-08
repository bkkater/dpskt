package br.edu.iff.ccc.bsi.dpskt_api.service;

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

    public boolean hasPendingClock(String discordId) {
        return clockRepository.findByUser_DiscordId(discordId).stream().anyMatch(clock -> clock.getEndAt() == null);
    }

    public Clock createClock(Clock clockModel) {
        return clockRepository.save(clockModel);
    }

    public boolean clockExists(UUID id) {
        return clockRepository.findById(id) != null;
    }

    public Clock patchClock(UUID id, Clock clockModel) {
        Optional<Clock> clock = clockRepository.findById(id);

        if (!clock.isPresent()) {
            return null;
        }

        if (clockModel.getEndAt() != null) {
            clock.get().setEndAt(clockModel.getEndAt());
        }

        return clockRepository.save(clock.get());
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
