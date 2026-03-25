package nextpos.app.nextpos.ai.prompt;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.AiPrompt;
import nextpos.app.nextpos.repository.AiPromptRepository;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptDataInitializer implements CommandLineRunner {
    private final AiPromptRepository promptRepository;

    @Override
    public void run(String... args) {
        if (promptRepository.count() == 0) {
            AiPrompt chatPrompt = new AiPrompt();
            chatPrompt.setPromptKey("chat_assistant");
            chatPrompt.setVersion(1);
            chatPrompt.setPromptTemplate(
                    "You are an AI assistant for an ERP system. User query: {{query}}\n\nContext: {{context}}");
            chatPrompt.setTemperature(BigDecimal.valueOf(0.7));
            chatPrompt.setMaxTokens(500);
            chatPrompt.setIsActive(true);
            chatPrompt.setDescription("Default chat assistant prompt");
            chatPrompt.setCreatedBy("system");
            promptRepository.save(chatPrompt);

            // Add product_generation prompt if needed
            AiPrompt productPrompt = new AiPrompt();
            productPrompt.setPromptKey("product_generation");
            productPrompt.setVersion(1);
            productPrompt.setPromptTemplate(
                    "Generate product details based on:\nName: {{product_name}}\nImage: {{product_image}}\n\nGenerate: description, category, tags, attributes, SEO metadata, suggested pricing, tax suggestion.");
            productPrompt.setTemperature(BigDecimal.valueOf(0.8));
            productPrompt.setMaxTokens(800);
            productPrompt.setIsActive(true);
            productPrompt.setDescription("Product generation prompt");
            productPrompt.setCreatedBy("system");
            promptRepository.save(productPrompt);
        }
    }
}