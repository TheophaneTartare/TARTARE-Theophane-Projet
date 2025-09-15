import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deprecation {

    public static void main(String[] args) throws IOException, ParseException {
        Directory index = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);

        String projectPath = "C:\\Users\\chris\\Documents\\GL\\eclipse-collections";
        indexJavaFiles(w, projectPath);
        w.close();

        findDeprecatedCode(index, analyzer);
    }

    private static void indexJavaFiles(IndexWriter w, String projectPath) throws IOException {
        Files.walk(Paths.get(projectPath))
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        String content = new String(Files.readAllBytes(path));
                        Document doc = new Document();
                        doc.add(new TextField("content", content, Field.Store.YES));
                        doc.add(new TextField("path", path.toString(), Field.Store.YES));
                        w.addDocument(doc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void findDeprecatedCode(Directory index, StandardAnalyzer analyzer) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse("@Deprecated");

        TopDocs results = searcher.search(query, Integer.MAX_VALUE);
        ScoreDoc[] hits = results.scoreDocs;

        int deprecatedMethodCount = 0; // Ajout du compteur

        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            String content = doc.get("content");
            String path = doc.get("path");
            String className = getClassNameFromPath(path);

            Pattern deprecatedElementPattern = Pattern.compile("@Deprecated\\s+(public|private|protected)?\\s+(class|interface|enum|@interface|\\w+\\s+\\w+\\s*\\([^\\)]*\\))");
            Matcher deprecatedElementMatcher = deprecatedElementPattern.matcher(content);

            while (deprecatedElementMatcher.find()) {
                String deprecatedElement = deprecatedElementMatcher.group();
                System.out.println("Code déprécié trouvé dans " + className + ": " + deprecatedElement);

                if (deprecatedElement.contains("(")) { // Vérifie si c'est une méthode
                    deprecatedMethodCount++; // Incrémente le compteur
                }

                String deprecatedCode = deprecatedElement.replaceAll("@Deprecated\\s+", "").trim();
                Pattern deprecatedCallPattern = Pattern.compile("\\b" + Pattern.quote(deprecatedCode.split("\\(")[0].trim()) + "\\b");
                Matcher deprecatedCallMatcher = deprecatedCallPattern.matcher(content);

                while (deprecatedCallMatcher.find()) {
                    if (!deprecatedElement.contains(deprecatedCallMatcher.group())) {
                        System.out.println("  Appel au code déprécié trouvé dans " + className + ": " + deprecatedCallMatcher.group());
                    }
                }
            }
        }

        System.out.println("\nNombre total de méthodes dépréciées : " + deprecatedMethodCount); // Affiche le compteur
        reader.close();
    }

    private static String getClassNameFromPath(String path) {
        String[] parts = path.split("\\\\");
        String fileName = parts[parts.length - 1];
        return fileName.replace(".java", "");
    }
}