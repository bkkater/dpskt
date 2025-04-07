package br.edu.iff.ccc.bsi.dpskt_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.edu.iff.ccc.bsi.dpskt_api.entities.Clock;
import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.repository.ClockRepository;

class ClockServiceTest {

  @Mock
  private ClockRepository clockRepository;

  @InjectMocks
  private ClockService clockService;

  private Clock mockClock;
  private User mockUser;
  private final UUID TEST_CLOCK_ID = UUID.randomUUID();
  private final String TEST_DISCORD_ID = "123456789012345678";
  private final LocalDateTime TEST_START_TIME = LocalDateTime.now().minusHours(2);
  private final LocalDateTime TEST_END_TIME = LocalDateTime.now();

  @SuppressWarnings("unused")
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    mockUser = new User();
    mockUser.setDiscordId(TEST_DISCORD_ID);
    mockUser.setIsAdmin(false);

    mockClock = createTestClock(TEST_CLOCK_ID, mockUser, TEST_START_TIME, null);
  }

  private Clock createTestClock(UUID id, User user, LocalDateTime startAt, LocalDateTime endAt) {
    Clock clock = new Clock();
    clock.setId(id);
    clock.setUser(user);
    clock.setStartAt(startAt);
    clock.setEndAt(endAt);
    return clock;
  }

  @Test
  void testFindAllClocks() {

    List<Clock> mockClocks = new ArrayList<>();
    mockClocks.add(mockClock);
    when(clockRepository.findAll()).thenReturn(mockClocks);

    List<Clock> result = clockService.findAllClocks();

    assertEquals(1, result.size());
    assertEquals(mockClock, result.get(0));
    verify(clockRepository).findAll();
  }

  @Test
  void testFindClockByUser() {

    List<Clock> mockClocks = new ArrayList<>();
    mockClocks.add(mockClock);
    when(clockRepository.findByUser_DiscordId(TEST_DISCORD_ID)).thenReturn(mockClocks);

    List<Clock> result = clockService.findClockByUser(TEST_DISCORD_ID);

    assertEquals(1, result.size());
    assertEquals(mockClock, result.get(0));
    verify(clockRepository).findByUser_DiscordId(TEST_DISCORD_ID);
  }

  @Test
  void testHasPendingClock_WhenUserHasPendingClock() {

    List<Clock> mockClocks = new ArrayList<>();

    mockClocks.add(mockClock);
    when(clockRepository.findByUser_DiscordId(TEST_DISCORD_ID)).thenReturn(mockClocks);

    boolean result = clockService.hasPendingClock(TEST_DISCORD_ID);

    assertTrue(result);
    verify(clockRepository).findByUser_DiscordId(TEST_DISCORD_ID);
  }

  @Test
  void testHasPendingClock_WhenUserHasNoClocks() {

    List<Clock> mockClocks = new ArrayList<>();
    when(clockRepository.findByUser_DiscordId(TEST_DISCORD_ID)).thenReturn(mockClocks);

    boolean result = clockService.hasPendingClock(TEST_DISCORD_ID);

    assertFalse(result);
    verify(clockRepository).findByUser_DiscordId(TEST_DISCORD_ID);
  }

  @Test
  void testHasPendingClock_WhenUserHasOnlyCompletedClocks() {

    List<Clock> mockClocks = new ArrayList<>();

    Clock completedClock = createTestClock(TEST_CLOCK_ID, mockUser, TEST_START_TIME, TEST_END_TIME);
    mockClocks.add(completedClock);
    when(clockRepository.findByUser_DiscordId(TEST_DISCORD_ID)).thenReturn(mockClocks);

    boolean result = clockService.hasPendingClock(TEST_DISCORD_ID);

    assertFalse(result);
    verify(clockRepository).findByUser_DiscordId(TEST_DISCORD_ID);
  }

  @Test
  void testCreateClock() {

    when(clockRepository.save(mockClock)).thenReturn(mockClock);

    Clock result = clockService.createClock(mockClock);

    assertEquals(mockClock, result);
    verify(clockRepository).save(mockClock);
  }

  @Test
  void testPatchClock_WhenClockExists() {

    Clock existingClock = createTestClock(TEST_CLOCK_ID, mockUser, TEST_START_TIME, null);

    Clock updateClock = new Clock();
    updateClock.setEndAt(TEST_END_TIME);

    when(clockRepository.findById(TEST_CLOCK_ID)).thenReturn(Optional.of(existingClock));
    when(clockRepository.save(any(Clock.class))).thenAnswer(i -> i.getArgument(0));

    Clock result = clockService.patchClock(TEST_CLOCK_ID, updateClock);

    assertNotNull(result);
    assertEquals(TEST_END_TIME, result.getEndAt());
    verify(clockRepository).findById(TEST_CLOCK_ID);
    verify(clockRepository).save(existingClock);
  }

  @Test
  void testPatchClock_WhenClockDoesNotExist() {
    when(clockRepository.findById(TEST_CLOCK_ID)).thenReturn(Optional.empty());

    Clock result = clockService.patchClock(TEST_CLOCK_ID, new Clock());

    assertNull(result);
    verify(clockRepository).findById(TEST_CLOCK_ID);
    verify(clockRepository, never()).save(any(Clock.class));
  }

  @Test
  void testDeleteClock_WhenClockExists() {

    when(clockRepository.findById(TEST_CLOCK_ID)).thenReturn(Optional.of(mockClock));
    doNothing().when(clockRepository).deleteById(TEST_CLOCK_ID);

    Optional<Clock> result = clockService.deleteClock(TEST_CLOCK_ID);

    assertTrue(result.isPresent());
    assertEquals(mockClock, result.get());
    verify(clockRepository).findById(TEST_CLOCK_ID);
    verify(clockRepository).deleteById(TEST_CLOCK_ID);
  }

  @Test
  void testDeleteClock_WhenClockDoesNotExist() {
    when(clockRepository.findById(TEST_CLOCK_ID)).thenReturn(Optional.empty());

    Optional<Clock> result = clockService.deleteClock(TEST_CLOCK_ID);

    assertTrue(result.isEmpty());
    verify(clockRepository).findById(TEST_CLOCK_ID);
    verify(clockRepository, never()).deleteById(any());
  }
}