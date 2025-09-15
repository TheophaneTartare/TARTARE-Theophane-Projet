import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageAnalyzer {

    public static void main(String[] args) throws Exception {
        Directory index = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);

        String projectPath = "C:\\Users\\chris\\Documents\\GL\\eclipse-collections"; // Remplace par le chemin de ton projet
        indexJavaFiles(w, projectPath);
        w.close();

        analyzePackages(index, analyzer);
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

    private static void analyzePackages(Directory index, StandardAnalyzer analyzer) throws Exception {
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);

        Set<String> packageNames = extractUniquePackageNames(searcher, analyzer);
        System.out.println("Nombre de paquetages uniques : " + packageNames.size());

        reader.close();
    }

    private static Set<String> extractUniquePackageNames(IndexSearcher searcher, StandardAnalyzer analyzer) throws Exception {
        Set<String> packageNames = new HashSet<>();
        Query query = new QueryParser("content", analyzer).parse("package");
        TopDocs docs = searcher.search(query, Integer.MAX_VALUE);
        for (org.apache.lucene.search.ScoreDoc hit : docs.scoreDocs) {
            Document doc = searcher.doc(hit.doc);
            String content = doc.get("content");
            String packageName = extractPackageName(content);
            if (packageName != null) {
                packageNames.add(packageName);
            }
        }
        return packageNames;
    }

    private static String extractPackageName(String content) {
        Pattern pattern = Pattern.compile("package\\s+([a-zA-Z0-9\\.]+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}