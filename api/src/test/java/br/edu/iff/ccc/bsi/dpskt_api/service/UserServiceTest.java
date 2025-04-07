package br.edu.iff.ccc.bsi.dpskt_api.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import br.edu.iff.ccc.bsi.dpskt_api.entities.Player;
import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.enums.Corporation;
import br.edu.iff.ccc.bsi.dpskt_api.enums.Role;
import br.edu.iff.ccc.bsi.dpskt_api.repository.UserRepository;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User mockUser;
  private Player mockPlayer;
  private final String TEST_DISCORD_ID = "123456789012345678";
  private final String NONEXISTENT_ID = "000000000000000000";

  @BeforeEach
  @SuppressWarnings("unused")
  void setUp() {
    MockitoAnnotations.openMocks(this);

    mockPlayer = createTestPlayer("1", "Test", Corporation.DPSKT, Role.RECRUTA, false);

    mockUser = new User();
    mockUser.setDiscordId(TEST_DISCORD_ID);
    mockUser.setIsAdmin(false);
    mockUser.setPlayer(mockPlayer);
  }

  private Player createTestPlayer(String id, String name, Corporation corp, Role role, Boolean statusClock) {
    Player player = new Player();
    player.setPlayerId(id);
    player.setName(name);
    player.setCorporation(corp);
    player.setRole(role);
    player.setStatusClock(statusClock);
    return player;
  }

  @Test
  void testFindAllUsers() {

    List<User> mockUsers = new ArrayList<>();
    mockUsers.add(mockUser);
    when(userRepository.findAll()).thenReturn(mockUsers);

    List<User> result = userService.findAllUsers();

    assertEquals(1, result.size());
    assertEquals(mockUser, result.get(0));
    verify(userRepository).findAll();
  }

  @Test
  void testUserExists_WhenUserExists() {

    when(userRepository.findByDiscordId(TEST_DISCORD_ID)).thenReturn(mockUser);

    boolean result = userService.userExists(TEST_DISCORD_ID);

    assertTrue(result);
    verify(userRepository).findByDiscordId(TEST_DISCORD_ID);
  }

  @Test
  void testUserExists_WhenUserDoesNotExist() {

    when(userRepository.findByDiscordId(NONEXISTENT_ID)).thenReturn(null);

    boolean result = userService.userExists(NONEXISTENT_ID);

    assertFalse(result);
    verify(userRepository).findByDiscordId(NONEXISTENT_ID);
  }

  @Test
  void testFindByDiscordId() {

    when(userRepository.findByDiscordId(TEST_DISCORD_ID)).thenReturn(mockUser);

    User result = userService.findByDiscordId(TEST_DISCORD_ID);

    assertEquals(mockUser, result);
    verify(userRepository).findByDiscordId(TEST_DISCORD_ID);
  }

  @Test
  void testCreateUser() {

    when(userRepository.save(mockUser)).thenReturn(mockUser);

    User result = userService.createUser(mockUser);

    assertEquals(mockUser, result);
    verify(userRepository).save(mockUser);
  }

  @Test
  void testPatch_UpdateIsAdmin() {

    User existingUser = new User();
    existingUser.setDiscordId(TEST_DISCORD_ID);
    existingUser.setIsAdmin(false);
    existingUser.setPlayer(mockPlayer);

    User updateUser = new User();
    updateUser.setIsAdmin(true);

    when(userRepository.findByDiscordId(TEST_DISCORD_ID)).thenReturn(existingUser);
    when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

    User result = userService.patch(TEST_DISCORD_ID, updateUser);

    assertTrue(result.getIsAdmin());
    verify(userRepository).findByDiscordId(TEST_DISCORD_ID);
    verify(userRepository).save(existingUser);
  }

  @Test
  void testPatch_UpdatePlayer() {

    Player existingPlayer = createTestPlayer("1", "Test", Corporation.DPSKT, Role.RECRUTA, false);

    User existingUser = new User();
    existingUser.setDiscordId(TEST_DISCORD_ID);
    existingUser.setIsAdmin(false);
    existingUser.setPlayer(existingPlayer);

    Player updatePlayer = createTestPlayer("2", "Test 2", Corporation.FBI, Role.SOLDADO, false);

    User updateUser = new User();
    updateUser.setPlayer(updatePlayer);

    when(userRepository.findByDiscordId(TEST_DISCORD_ID)).thenReturn(existingUser);
    when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

    User result = userService.patch(TEST_DISCORD_ID, updateUser);

    Player resultPlayer = result.getPlayer();
    assertEquals("2", resultPlayer.getPlayerId());
    assertEquals("Test 2", resultPlayer.getName());
    assertEquals(Corporation.FBI, resultPlayer.getCorporation());
    assertEquals(Role.SOLDADO, resultPlayer.getRole());
    assertFalse(resultPlayer.getStatusClock());
    verify(userRepository).findByDiscordId(TEST_DISCORD_ID);
    verify(userRepository).save(existingUser);
  }

  @Test
  void testPatch_PartialPlayerUpdate() {

    Player existingPlayer = new Player();
    existingPlayer.setPlayerId("1");
    existingPlayer.setName("Original Name");
    existingPlayer.setCorporation(Corporation.DPSKT);
    existingPlayer.setRole(Role.RECRUTA);
    existingPlayer.setStatusClock(false);

    User existingUser = new User();
    existingUser.setDiscordId(TEST_DISCORD_ID);
    existingUser.setIsAdmin(false);
    existingUser.setPlayer(existingPlayer);

    // Criando um objeto de atualização apenas com nome
    Player updatePlayer = new Player();
    updatePlayer.setName("New Name");

    User updateUser = new User();
    updateUser.setPlayer(updatePlayer);

    when(userRepository.findByDiscordId(TEST_DISCORD_ID)).thenReturn(existingUser);
    when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

    User result = userService.patch(TEST_DISCORD_ID, updateUser);

    Player resultPlayer = result.getPlayer();
    assertEquals("1", resultPlayer.getPlayerId());
    assertEquals("New Name", resultPlayer.getName());
    assertEquals(Corporation.DPSKT, resultPlayer.getCorporation());
    assertEquals(Role.RECRUTA, resultPlayer.getRole());
    assertFalse(resultPlayer.getStatusClock());
    verify(userRepository).findByDiscordId(TEST_DISCORD_ID);
    verify(userRepository).save(existingUser);
  }

  @Test
  void testDeleteUser_WhenUserExists() {

    when(userRepository.findByDiscordId(TEST_DISCORD_ID)).thenReturn(mockUser);
    doNothing().when(userRepository).delete(mockUser);

    User result = userService.deleteUser(TEST_DISCORD_ID);

    assertEquals(mockUser, result);
    verify(userRepository).findByDiscordId(TEST_DISCORD_ID);
    verify(userRepository).delete(mockUser);
  }

  @Test
  void testDeleteUser_WhenUserDoesNotExist() {

    when(userRepository.findByDiscordId(NONEXISTENT_ID)).thenReturn(null);

    User result = userService.deleteUser(NONEXISTENT_ID);

    assertNull(result);
    verify(userRepository).findByDiscordId(NONEXISTENT_ID);
    verify(userRepository, never()).delete(any(User.class));
  }
}