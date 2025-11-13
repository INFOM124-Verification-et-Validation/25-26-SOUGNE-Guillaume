package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class ClydeTest {
    private final PacManSprites pacManSprites = new PacManSprites();
    private final PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
    private final GhostFactory ghostFactory = new GhostFactory(pacManSprites);
    private final GhostMapParser ghostMapParser = new GhostMapParser(new LevelFactory(pacManSprites, ghostFactory), new BoardFactory(pacManSprites), ghostFactory);

    /*
    ###
    #P#
    # #
    # #
     C
    ###
     */
    private static final char[][] FREEMAPMINUS8 = new char[][]{"#### #".toCharArray(),"#P  C#".toCharArray(),"##### #".toCharArray()};
    private static final char[][] FREEMAPPLUS8 = new char[][]{"############".toCharArray()," P        C ".toCharArray(),"############".toCharArray()};
    private static final char[][] NOPLAYERMAP = new char[][]{"C  ".toCharArray(),"   ".toCharArray(),"   ".toCharArray()};
    private static final char[][] NOMOVEMAP = new char[][]{"#######".toCharArray(),"#C###P#".toCharArray(),"#######".toCharArray()};



    public Level setupLevel(char[][] map, boolean withPacMan, Direction direction) {
        Level level = ghostMapParser.parseMap(map);
        if (withPacMan) {
            Player player = playerFactory.createPacMan();
            player.setDirection(Objects.requireNonNullElse(direction, Direction.NORTH));
            level.registerPlayer(player);
        }
        return level;
    }

    @Test
    public void closeOpenPath() {
        Level level = setupLevel(FREEMAPMINUS8, true, Direction.WEST);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        Assert.assertNotNull(clyde);
        Optional<Direction> direction = clyde.nextAiMove();
        Assert.assertTrue(direction.isPresent() && direction.get()==Direction.SOUTH);
    }

    @Test
    public void farOpenPath() {
        Level level = setupLevel(FREEMAPPLUS8, true, Direction.WEST);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        Assert.assertNotNull(clyde);
        Optional<Direction> direction = clyde.nextAiMove();
        Assert.assertTrue(direction.isPresent() && direction.get()==Direction.NORTH);
    }

    @Test
    public void noPacMan() {
        Level level = setupLevel(NOPLAYERMAP, false, Direction.WEST);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        Assert.assertNotNull(clyde);
        Optional<Direction> direction = clyde.nextAiMove();
        Assert.assertFalse(direction.isPresent());
    }

    @Test
    public void noMove() {
        Level level = setupLevel(NOMOVEMAP, true, Direction.WEST);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        Assert.assertNotNull(clyde);
        Optional<Direction> direction = clyde.nextAiMove();
        Assert.assertFalse(direction.isPresent());
    }
}
