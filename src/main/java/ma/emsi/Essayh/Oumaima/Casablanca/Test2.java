package ma.emsi.Essayh.Oumaima.Casablanca;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.Map;

public class Test2 {
    public static void main(String[] args) {

        String texte = "Bonjour, je m'appelle Oumaima essayh , j'ai 23 ans ";

        Prompt prompt = PromptTemplate
                .from("""
                Traduisez en {{langue}} le texte suivant : {{texte}}""")
                .apply(Map.of(
                        "langue", "allemand",
                        "texte", texte
                ));

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .temperature(0.7)
                .modelName("gemini-1.5-flash")
                .apiKey(System.getenv("GEMINI_KEY"))
                .build();

        System.out.println(model.generate(prompt.text()));

    }
}
