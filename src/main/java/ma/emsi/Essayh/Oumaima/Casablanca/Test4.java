package ma.emsi.Essayh.Oumaima.Casablanca;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

public class Test4 {
    public static void main(String[] args) {
        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .temperature(0.7)
                .modelName("gemini-1.5-flash")
                .apiKey(System.getenv("GEMINI_KEY"))
                .build();

        String question = "Comment s'appelle le chat de Pierre?";
        System.out.printf(model.generate(question));
    }
}
