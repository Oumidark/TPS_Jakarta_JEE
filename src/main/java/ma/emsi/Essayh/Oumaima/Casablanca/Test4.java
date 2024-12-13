package ma.emsi.Essayh.Oumaima.Casablanca;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class Test4 {
        // Assistant conversationnel
        interface Assistant {
            // Prend un message de l'utilisateur et retourne une réponse du LLM.
            String chat(String userMessage);
        }

        public static void main(String[] args) {
            String llmKey =System.getenv("GEMINI_KEY");
            ChatLanguageModel model =GoogleAiGeminiChatModel.builder()
                    .temperature(0.7)
                    .modelName("gemini-1.5-flash")
                    .apiKey(llmKey)
                    .build();

//            // Chargement du document, sous la forme d'embeddings, dans une base vectorielle en mémoire
//            String nomDocument = "infos.txt";
//            Document document = FileSystemDocumentLoader.loadDocument(nomDocument);
//            InMemoryEmbeddingStore embeddingStore = new InMemoryEmbeddingStore<>();
//            // Calcule les embeddings et les enregistre dans la base vectorielle
//            EmbeddingStoreIngestor.ingest(document, embeddingStore);
            // Chargement du document, sous la forme d'embeddings, dans une base vectorielle en mémoire
            String nomDocument = "Cours-Machine-learning.pdf";
            Document document = FileSystemDocumentLoader.loadDocument(nomDocument);
            InMemoryEmbeddingStore embeddingStore = new InMemoryEmbeddingStore<>();
            // Calcule les embeddings et les enregistre dans la base vectorielle
            EmbeddingStoreIngestor.ingest(document, embeddingStore);

            // Création de l'assistant conversationnel, avec une mémoire.
            // L'implémentation de Assistant est faite par LangChain4j.
            // La base vectorielle en mémoire est utilisée pour retrouver les embeddings.
            Assistant assistant =
                    AiServices.builder(Assistant.class)
                            .chatLanguageModel(model)
                            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                            .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                            .build();

            // Le LLM va utiliser l'information du fichier infos.txt pour répondre à la question.
            String question = "Quel est l'objectif du cours 'Machine Learning' de Richard Grin ?";
            // L'assistant analyse la question et recherche les informations pertinentes
            // pour la question dans la base vectorielle.
            // Ces informations pertinentes sont ajoutées à la question et le tout est envoyé au LLM.
            String reponse = assistant.chat(question);
            // Affiche la réponse du LLM.
            System.out.println(reponse);
        }
}
