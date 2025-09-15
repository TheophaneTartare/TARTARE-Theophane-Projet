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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GodClasse {

    public static void main(String[] args) throws IOException, ParseException {
        Directory index = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);

        String projectPath = "C:\\Users\\chris\\Documents\\GL\\eclipse-collections"; // Remplace par le chemin de ton projet
        indexJavaFiles(w, projectPath);
        w.close();

        analyzeClasses(index, analyzer);
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

    private static void analyzeClasses(Directory index, StandardAnalyzer analyzer) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse("public class");

        TopDocs results = searcher.search(query, Integer.MAX_VALUE);
        ScoreDoc[] hits = results.scoreDocs;

        List<Integer> methodCounts = new ArrayList<>();
        List<Integer> variableCounts = new ArrayList<>();
        List<Integer> lineCounts = new ArrayList<>();
        int zeroMethodCount = 0;
        int maxMethodCount = 0;
        String maxMethodClass = "";

        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            String content = doc.get("content");
            String path = doc.get("path");
            String className = getClassNameFromPath(path);

            int methodCount = countMethods(content);
            int variableCount = countVariables(content);
            int lineCount = countLines(content);

            methodCounts.add(methodCount);
            variableCounts.add(variableCount);
            lineCounts.add(lineCount);

            if (methodCount == 0) {
                zeroMethodCount++;
            }

            if (methodCount > maxMethodCount) {
                maxMethodCount = methodCount;
                maxMethodClass = className;
            }
        }

        printAverage("Méthodes", methodCounts);
        printAverage("Variables", variableCounts);
        printAverage("Lignes", lineCounts);
        System.out.println("Nombre de classes avec 0 méthodes : " + zeroMethodCount);
        System.out.println("Nombre maximum de méthodes dans une classe : " + maxMethodCount + " (" + maxMethodClass + ")");

        reader.close();
    }

    private static int countMethods(String content) {
        Pattern pattern = Pattern.compile("(public|private|protected|static|\\s)+[\\w<>\\[\\]]+\\s+\\w+\\s*\\([^)]*\\)\\s*(throws [\\w<>\\[\\], ]+)?\\s*\\{", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int countVariables(String content) {
        Pattern pattern = Pattern.compile("(public|private|protected|static|final|\\s)\\s+[\\w<>\\[\\]]+\\s+\\w+\\s*(=\\s*[^;]+)?;");
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int countLines(String content) {
        return content.split("\\r?\\n").length;
    }

    private static void printAverage(String name, List<Integer> counts) {
        if (counts.isEmpty()) return;
        double average = counts.stream().mapToInt(val -> val).average().orElse(0.0);
        System.out.println("Moyenne " + name + " : " + average);
    }

    private static String getClassNameFromPath(String path) {
        String[] parts = path.split("\\\\");
        String fileName = parts[parts.length - 1];
        return fileName.replace(".java", "");
    }
}