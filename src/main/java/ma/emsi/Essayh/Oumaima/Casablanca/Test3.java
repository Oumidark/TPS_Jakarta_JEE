package ma.emsi.Essayh.Oumaima.Casablanca;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;

public class Test3
{
    public static void main(String[] args) {

        EmbeddingModel embeddingModel = GoogleAiEmbeddingModel.builder()
                .apiKey(System.getenv("GEMINI_KEY"))
                .modelName("text-embedding-004")
                .taskType(GoogleAiEmbeddingModel.TaskType.SEMANTIC_SIMILARITY)
                .outputDimensionality(300)
                .timeout(Duration.ofSeconds(10))
                .build();

        String Text1, Text2;
        Text1= "Il fait beau aujourd'hui";
        Text2="La méteo annonce un beau temps";

        Embedding embedding1 = embeddingModel.embed(Text1).content();
        Embedding embedding2 = embeddingModel.embed(Text2).content();

        System.out.println(CosineSimilarity.between(embedding1,embedding2));



    }
}
