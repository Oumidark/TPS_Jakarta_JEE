package ma.emsi.essayh.oumaima.casablanca.tp2essayhoumaima14partie2.jsf;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.emsi.essayh.oumaima.casablanca.tp2essayhoumaima14partie2.llm.LlmClient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class Bb implements Serializable {
    private String systemRole = "helpful assistant";
    private boolean systemRoleChangeable = true;
    private String question;
    private String reponse;
    private StringBuilder conversation = new StringBuilder();
    @Inject
    private FacesContext facesContext;
    @Inject
    private LlmClient llmClient;

    public Bb() {
    }

    public String getSystemRole() {
        return this.systemRole;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    public boolean isSystemRoleChangeable() {
        return this.systemRoleChangeable;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return this.reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getConversation() {
        return this.conversation.toString();
    }

    public void setConversation(String conversation) {
        this.conversation = new StringBuilder(conversation);
    }

    public String envoyer() {
        if (this.question != null && !this.question.isBlank()) {
            this.llmClient.setSystemRole(this.systemRole);
            this.reponse = this.llmClient.envoyer(this.question);
            this.afficherConversation();
            return null;
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Texte question vide", "Il manque le texte de la question");
            this.facesContext.addMessage((String)null, message);
            return null;
        }
    }

    public String nouveauChat() {
        return "index";
    }

    private void afficherConversation() {
        this.conversation.append("== User:\n").append(this.question).append("\n== Serveur:\n").append(this.reponse).append("\n");
    }

    public List<SelectItem> getSystemRoles() {
        List<SelectItem> listeSystemRoles = new ArrayList();
        String role = "You are a helpful assistant. You help the user to find the information they need.\nIf the user type a question, you answer it.\n";
        listeSystemRoles.add(new SelectItem(role, "Assistant"));
        role = "You are an interpreter. You translate from English to French and from French to English.\nIf the user type a French text, you translate it into English.\nIf the user type an English text, you translate it into French.\nIf the text contains only one to three words, give some examples of usage of these words in English.\n";
        listeSystemRoles.add(new SelectItem(role, "Traducteur Anglais-Français"));
        role = "Your are a travel guide. If the user type the name of a country or of a town,\nyou tell them what are the main places to visit in the country or the town\nare you tell them the average price of a meal.\n";
        listeSystemRoles.add(new SelectItem(role, "Guide touristique"));
        listeSystemRoles.add(new SelectItem(role, "Nutrition Coach"));
        role = "You are a Nutrition Coach. If the user asks you about nutrition, start by explaining the basics of healthy eating, the importance of balanced diets, and the role of macronutrients (carbohydrates, proteins, fats).\nMake it engaging by including fun facts about food, tips for meal prep, and how to maintain a sustainable diet. Provide practical advice on how to set achievable dietary goals and share small, actionable steps (e.g., swapping sugary drinks for water, adding more greens to meals).\nIf possible, tailor your responses to their fitness goals ( muscle gain, weight loss, or energy boosting.....).\n";
        this.systemRole = (String)((SelectItem)listeSystemRoles.get(0)).getValue();
        return listeSystemRoles;
    }
}


