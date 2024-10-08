package it.unipd.dei.dbdc.analysis;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class that tests {@link Article}.
 * It doesn't test setters and getters as they are very simple functions.
 */
@Order(7)
public class ArticleTest {

    /**
     * Tests the various constructors of {@link Article}.
     */
    @Test
    public void Article() {
        Article art = new Article("a", "b", "c", "d", "e", "f", "g");
        assertEquals("a", art.getID());
        assertEquals("b", art.getURL());
        assertEquals("c", art.getTitle());
        assertEquals("d", art.getBody());
        assertEquals("e", art.getDate());
        assertEquals("f", art.getSourceSet());
        assertEquals("g", art.getSource());

        art = new Article();
            assertNull(art.getID());
            assertNull(art.getURL());
            assertNull(art.getTitle());
            assertNull(art.getBody());
            assertNull(art.getDate());
            assertNull(art.getSourceSet());
            assertNull(art.getSource());

        String[] params = {"a1", "b1", "c1", "d1", "e1", "f1", "g1"};
        art = new Article(params);
        assertEquals("a1", art.getID());
        assertEquals("b1", art.getURL());
        assertEquals("c1", art.getTitle());
        assertEquals("d1", art.getBody());
        assertEquals("e1", art.getDate());
        assertEquals("f1", art.getSourceSet());
        assertEquals("g1", art.getSource());

        //Initializes with a number of parameters that are not legal
        String[] par = {null, null, null, null};
        assertThrows(IllegalArgumentException.class, () -> new Article(par));

        String[] par1 = {"null", "null", "null", "null"};
        assertThrows(IllegalArgumentException.class, () -> new Article(par1));

        assertThrows(IllegalArgumentException.class, () -> new Article(null));
    }

    /**
     * Tests {@link Article#obtainText()} with even null strings.
     */
    @Test
    public void obtainText()
    {
        String[] params = {"a1", "b1", "c1", "d1", "e1", "f1", "g1"};
        Article article = new Article(params);
        assertEquals("c1 d1", article.obtainText());

        String[] params1 = {"a1", "b1", null, null, "e1", "f1", "g1"};
        final Article article1 = new Article(params1);
        assertThrows(IllegalArgumentException.class, article1::obtainText);

        article.setBody("");
        article.setTitle("");
        assertEquals(" ", article.obtainText());
    }

    /**
     * Tests of {@link Article#toString()}.
     */
    @Test
    public void string() {
        String[] params = {"a1", "b1", "c1", "d1", "e1", "f1", "g1"};
        Article article = new Article(params);

        String expected = "Article{" +
                "ID='" + params[0] + '\'' +
                ", URL='" + params[1] + '\'' +
                ", title='" + params[2] + '\'' +
                ", body='" + params[3] + '\'' +
                ", Date='" + params[4] + '\'' +
                ", SourceSet='" + params[5] + '\'' +
                ", Source='" + params[6] + '\'' +
                '}';
        assertEquals(expected, article.toString());
    }

    /**
     * Tests of {@link Article#equals(Object)}.
     */
    @Test
    public void equals() {
        String[] params = {"a1", "b1", "c1", "d1", "e1", "f1", "g1"};
        Article article = new Article(params);
        Article article1 = new Article(params);
        assertTrue(article1.equals(article));

        article.setBody("z1");
        assertFalse(article1.equals(article));

        //This also tests if there are any null exceptions
        article1 = new Article();
        assertFalse(article1.equals(article));

        article = new Article();
        assertTrue(article1.equals(article));

        assertFalse(article1.equals(null));
    }

    /**
     * The only constructor of the class. It is declared as private to
     * prevent the default constructor to be created.
     */
    private ArticleTest() {}
}