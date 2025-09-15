import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class LuceneExample {

    public static void main(String[] args) throws Exception {

        // 1. Crée un index en mémoire
        Directory index = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);
        addDoc(w, "Lucene in Action");
        addDoc(w, "Lucene for Dummies");
        addDoc(w, "Managing Gigabytes");
        addDoc(w, "The Art of Computer Science");
        w.close();

        // 2. Recherche dans l'index
        String querystr = args.length > 0 ? args[0] : "lucene";
        Query q = new QueryParser("title", analyzer).parse(querystr);

        // 3. Effectue la recherche
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        // 4. Affiche les résultats
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title"));
        }

        reader.close();
    }

    private static void addDoc(IndexWriter w, String title) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        w.addDocument(doc);
    }
}