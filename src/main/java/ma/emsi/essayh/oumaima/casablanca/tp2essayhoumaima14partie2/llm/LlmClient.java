package ma.emsi.essayh.oumaima.casablanca.tp2essayhoumaima14partie2.llm;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.Dependent;
import java.io.Serializable;

@Dependent
public class LlmClient implements Serializable {
    private String systemRole;
    private Assistant assistant;
    private ChatMemory chatMemory;
    public LlmClient() {
        String llmKey = System.getenv("GEMINI_KEY");
        ChatLanguageModel model = GoogleAiGeminiChatModel.builder().apiKey(llmKey).modelName("gemini-1.5-flash").temperature(0.7).build();
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        this.assistant = (Assistant)AiServices.builder(Assistant.class).chatLanguageModel(model).chatMemory(this.chatMemory).build();
    }
    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
        this.chatMemory.clear();
        this.chatMemory.add(new SystemMessage(this.systemRole));
    }

    public String envoyer(String requete) {
        return this.assistant.chat(requete);
    }

    public interface Assistant {
        String chat(String var1);
    }
}