
import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Simple HelloWorld program (clear of Checkstyle and FindBugs warnings).
 *
 * @author Mohamed Mohamed
 */
public final class Glossary {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private Glossary() {
        // no code needed here
    }

    /**
     * Generates terms and their definitions in input into MAP. it Also
     * generates a Queue of all the terms in no particular order into the given
     * queue.
     *
     * @param input
     *            the inputed file
     * @param MAP
     *            where terms and definition to be placed
     * @param terms
     *            the {@code Queue} to be replaced
     * @replaces {@code MAP}
     * @replaces {@code terms}
     * @ensures <pre>
     * {@code MAP = elements(input)} &&{@code terms = elements(input)}
     * </pre>
     */
    private static void generateTerms(SimpleReader input,
            Map<String, String> MAP, Sequence<String> terms) {

        String def1 = "";
        String def2 = "";
        String line;

        int i = 0;
        int count = 0;
        while (!input.atEOS()) {

            line = input.nextLine();

            terms.add(i, line);
            i++;

            //making def2 empty, and def1 into anything that will be replace
            def2 = "";
            def1 = "reset";
            //this makes sure the complete definition is
            //copied(when definition is 1<lines)
            while (!def1.isEmpty()) {
                def1 = input.nextLine();
                def2 = def2.concat(def1);
            }

            MAP.add(line, def2);

        }

    }

    /**
     * Alphabetizes the given {@code Sequence}. Alphabetizes the given
     * {@code Map}.
     *
     * @param terms
     *            the given {@code Sequence}
     * @param map
     *            the given {@code Map}
     *
     * @replaces {@code map}
     * @replaces {@code terms}
     */

    private static void alphabetizingMap(Sequence<String> terms,
            Map<String, String> map) {

        Map<String, String> tempMap = map.newInstance();
        Sequence<String> tempTerms = terms.newInstance();
        tempTerms.transferFrom(terms);

        int added = 0;
        int i = 0;
        String word = "";

        //loop until tempTerm.remove() reduces it to zero
        while (tempTerms.length() > 0) {
            int count = 0;

            int count2 = 0;
            while (count < tempTerms.length() - 1) {
                word = tempTerms.entry(count2);
                if (word.compareTo(tempTerms.entry(count + 1)) > 0) {
                    count2 = count + 1;
                }
                count++;
            }
            terms.add(added, tempTerms.entry(count2));

            tempTerms.remove(count2);

            added++;
        }
        int transfercount = 0;

        while (transfercount < terms.length()) {

            tempMap.add(terms.entry(transfercount),
                    map.value(terms.entry(transfercount)));

            transfercount++;

        }

        map.transferFrom(tempMap);

    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces strSet
     * @ensures strSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";

        strSet.clear();
        char strPiece = 'i';
        int i = 0;
        while (i < str.length()) {
            strPiece = str.charAt(i);
            if (!strSet.contains(strPiece)) {
                strSet.add(strPiece);
            }
            i++;
        }

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        char piece = text.charAt(position);
        String pieceStr = "" + piece;
        int i = 0;
        String result = "";
        if (separators.contains(text.charAt(position))) {

            while (i < text.substring(position).length()
                    && separators.contains(piece)) {
                piece = text.charAt(position + i);
                pieceStr = "" + piece;
                if (separators.contains(piece)) {
                    result = result.concat(pieceStr);
                }
                i++;
            }

        } else {

            while (i < text.substring(position).length()
                    && !(separators.contains(piece))) {
                piece = text.charAt(position + i);
                pieceStr = "" + piece;
                if (!separators.contains(piece)) {
                    result = result.concat(pieceStr);
                }
                i++;
            }

        }

        return result;
    }

    /**
     * Updates map values with html hyperlink tags if their words appear in
     * terms {@code Sequence}.
     *
     * @param map
     *            the given {@code Map}
     * @param terms
     *            the given {@code Sequence}
     * @param separatorSet
     *            holds separators like (,. !?/t)
     *
     * @restores {@code terms}
     * @replaces {@code map}
     */

    private static void mapUpdateLink(Map<String, String> map,

            Sequence<String> terms, Set<Character> separatorSet) {

        Map<String, String> copyMap = new Map1L<String, String>();

        Set<String> termsSet = new Set1L<String>();

        String tempTerm = "";

        int position = 0;

        int count = 0;

        int size = terms.length();

        //copy Sequence terms to Set termsSet
        while (termsSet.size() < size) {

            tempTerm = terms.remove(count);

            termsSet.add(tempTerm);

            terms.add(count, tempTerm);

            count++;

        }

        count = 0;

        String sentence = "";

        while (count < termsSet.size()) {

            position = 0;

            sentence = "";

            String testStr = map.value(terms.entry(count));

            while (position < testStr.length()) {

                String token = nextWordOrSeparator(testStr, position,

                        separatorSet);

                if (separatorSet.contains(token.charAt(0))) {

                    sentence = sentence + token;

                } else {
                    if (termsSet.contains(token)) {

                        sentence = sentence + "<a href=\"" + token + ".html\">"

                                + token + "</a>";

                    } else {

                        sentence = sentence.concat(token);

                    }
                }

                position += token.length();

            }

            copyMap.add(terms.entry(count), sentence);

            count++;

        }

        map.transferFrom(copyMap);
    }

    /**
     * Generates the index page that consists of the terms.
     *
     * @param terms
     *            the Sequence of terms
     * @param out
     *            the output stream
     * @updates {@code out.content}
     * @ensures <pre>
     * {@code out.content = #out.content * [the HTML opening tags]}
     * </pre>
     */

    private static void outputIndexPage(Sequence<String> terms,
            SimpleWriter out) {

        out.println("<html>");
        out.println("<head>");
        out.println("<title> Glossary </title>");

        out.println("</head>");
        out.println("<body>");

        out.println("<h2> Glossary </h2><hr/>");
        out.println("<h3> Index </h3>");
        out.println("<ul>");

        for (int i = 0; i < terms.length(); i++) {
            String list = terms.entry(i);
            out.println(
                    "<li><a href=\"" + list + ".html\">" + list + "</a></li>");
        }
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");

    }

    /**
     * Generates the pages for each of the terms from the input file.
     *
     * @param map
     *            the Map of terms and definitions
     * @param terms
     *            the Sequence of terms
     * @param folder
     *            the name of the folder the html files are output to
     * @updates {@code out.content}
     * @ensures <pre>
     * {@code out.content = #out.content * [the HTML tags]}
     * </pre>
     */

    private static void outputDefinitions(Sequence<String> terms,
            Map<String, String> map, String folder) {

        String file = "";
        int loop1 = terms.length();
        for (int k = 0; k < loop1; k++) {
            file = terms.entry(k);
            SimpleWriter out = new SimpleWriter1L(
                    folder + "/" + file + ".html");

            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + file + "</title>");

            out.println("</head>");
            out.println("<h2><b><i><font color=\"red\">" + file

                    + "</font></i></b></h2>");

            out.println("<blockquotes>" + map.value(terms.entry(k))
                    + "</blockquote><hr/>");
            out.println("<p>Return to <a href=\"index.html\">index</a></p>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        }

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();

        out.println("Enter the name of the input file");
        SimpleReader file = new SimpleReader1L(in.nextLine());

        out.println("Enter the name of output folder");
        String folder = in.nextLine();
        SimpleWriter indexPage = new SimpleWriter1L(folder + "/index.html");

        /*
         * Creates a Map with terms and definitions. Also creates a Sequence of
         * all the terms in no particular order.
         */
        Map<String, String> myMap = new Map1L<String, String>();
        Sequence<String> terms = new Sequence1L<>();
        generateTerms(file, myMap, terms);

        //arrange myMap and term in alphabetical order
        alphabetizingMap(terms, myMap);

        /*
         * generate separators and words
         */
        final String separatorStr = " .,";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separatorStr, separatorSet);

        //update map to contain links
        mapUpdateLink(myMap, terms, separatorSet);

        //index page
        outputIndexPage(terms, indexPage);

        /*
         * terms and definition pages
         */
        outputDefinitions(terms, myMap, folder);

        out.close();
        in.close();
        indexPage.close();

    }

}
