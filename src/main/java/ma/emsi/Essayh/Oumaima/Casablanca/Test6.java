package ma.emsi.Essayh.Oumaima.Casablanca;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Test6 {
    private static void configureLogger() {
        // Configure le logger sous-jacent (java.util.logging)
        Logger packageLogger = Logger.getLogger("dev.langchain4j");
        packageLogger.setLevel(Level.FINE); // Ajuster niveau
        // Ajouter un handler pour la console pour faire afficher les logs
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        packageLogger.addHandler(handler);
    }
    public static void main(String[] args) {
        configureLogger();

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .temperature(0.3)
                .modelName("gemini-1.5-flash")
                .apiKey(System.getenv("GEMINI_KEY"))
                .logRequestsAndResponses(true)
                .build();

        Path pathRag;
        try {
            String cheminRessource = "/rag.pdf";
            URL fileUrl = RagNaif.class.getResource(cheminRessource);
            pathRag = Paths.get(fileUrl.toURI());

        } catch (URISyntaxException e) {
            throw new RuntimeException(e); // ou un autre traitement du problème...
        }

        DocumentParser parser = new ApacheTikaDocumentParser();

        Document document2 = FileSystemDocumentLoader.loadDocument(pathRag, parser);

        DocumentSplitter documentSplitter = DocumentSplitters.recursive(200, 10);

        //List<TextSegment> chunks = documentSplitter.split(document);

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        //Response<List<Embedding>> embeddings = embeddingModel.embedAll(chunks);

        EmbeddingStore embeddingStore1 = new InMemoryEmbeddingStore();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore1)
                .embeddingModel(embeddingModel)
                .documentSplitter(documentSplitter)
                .build();

        ingestor.ingest(document2);


        ContentRetriever contentRetriever1 = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore1)
                .minScore(0.5)
                .maxResults(2)
                .build();

        WebSearchEngine searchEngine = TavilyWebSearchEngine.builder()
                .apiKey(System.getenv("TAVILY_KEY"))
                .build();

        ContentRetriever contentRetriever2 = WebSearchContentRetriever.builder()
                .maxResults(2)
                .webSearchEngine(searchEngine)
                .build();

        QueryRouter queryRouter = new DefaultQueryRouter(List.of(contentRetriever1, contentRetriever2));

        QueryTransformer queryTransformer = CompressingQueryTransformer.builder()
                .chatLanguageModel(model)
                .build();

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .queryTransformer(queryTransformer)
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10); //n'est pas utilisé

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .retrievalAugmentor(retrievalAugmentor)
                .build();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("==================================================");
                System.out.println("Posez votre question : ");
                String question = scanner.nextLine();
                System.out.println("==================================================");
                if ("fin".equalsIgnoreCase(question)) {
                    break;
                }
                String reponse = assistant.chat(question);
                System.out.println("==================================================");
                System.out.println("Assistant : " + reponse);
            }
        }

    }
}
