package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractCollisionTest {

    private final PlayerFactory playerFactory = new PlayerFactory(new PacManSprites());
    private final GhostFactory ghostFactory = new GhostFactory(new PacManSprites());
    private final BoardFactory boardFactory = new BoardFactory(new PacManSprites());
    private Pellet pellet;
    private Player player;
    private Ghost ghost;
    protected CollisionMap instance;


    @BeforeEach
    void setUp() {
        pellet = new Pellet(10, new PacManSprites().getPelletSprite());
        pellet.occupy(boardFactory.createGround());
        player = playerFactory.createPacMan();
        ghost = ghostFactory.createBlinky();
    }

    @Test
    void testPlayerPelletCollisions() {
        int initialPoints = player.getScore();
        instance.collide(player, pellet);
        assertFalse(pellet.hasSquare());
        assertEquals(player.getScore(), initialPoints + pellet.getValue(), "Player should gain points after colliding with pellet");
    }

    @Test
    void testPelletPlayerCollisions() {
        int initialPoints = player.getScore();
        instance.collide(pellet, player);
        assertEquals(player.getScore(), initialPoints + pellet.getValue(), "Player should gain points after colliding with pellet");
    }

    @Test
    void testPlayerGhostCollisions() {
        assertTrue(player.isAlive(), "Player should be alive before colliding with ghost");
        instance.collide(player, ghost);
        assertFalse(player.isAlive(), "Player should be dead after colliding with ghost");
    }

    @Test
    void testGhostPlayerCollisions() {
        assertTrue(player.isAlive(), "Player should be alive before colliding with ghost");
        instance.collide(ghost, player);
        assertFalse(player.isAlive(), "Player should be dead after colliding with ghost");
    }

    @Test
    void testGhostPelletCollisions() {
        assertTrue(pellet.hasSquare());
        instance.collide(ghost, pellet);
        assertTrue(pellet.hasSquare(), "Pellet should remain after ghost collides with it");
    }


}
