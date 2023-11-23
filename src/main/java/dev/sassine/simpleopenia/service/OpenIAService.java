package dev.sassine.simpleopenia.service;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import dev.sassine.simpleopenia.model.SizeImage;
import dev.sassine.simpleopenia.model.Weather;
import dev.sassine.simpleopenia.model.WeatherResponse;
import io.reactivex.Flowable;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class OpenIAService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.action}")
    private String promptsAction;

    @Value("${openai.model}")
    private String modelIA;

    private OpenAiService service;


    @PostConstruct
    public void init() {
        this.service = new OpenAiService(apiKey, Duration.ofSeconds(30));
    }

    public void gerarImagem(@NotNull String prompt, SizeImage tamanho, int numeroDeImagens) {
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(prompt)
                .n(Optional.ofNullable(numeroDeImagens).orElse(1))
                .size(Optional.ofNullable(tamanho).orElse(SizeImage.P).getSize())
                .user("s-dev")
                .build();

        System.out.println("\n Acesse a imagem gerada: ");
        service.createImage(request)
                .getData()
                .forEach(i ->System.out.println(i.getUrl()));
    }

    public void gerarResposta(@NotNull String prompt, @Nullable int maxTokens) {
        FunctionExecutor functionExecutor = new FunctionExecutor(Collections.singletonList(ChatFunction.builder()
                .name("get_weather")
                .description("Get the current weather of a location")
                .executor(Weather.class, w -> new WeatherResponse(w.location, w.unit, 150, "rainy"))
                .build()));

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), promptsAction);
        messages.add(systemMessage);

        ChatMessage firstMsg = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        messages.add(firstMsg);


            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model(modelIA) // gpt-4-1106-preview || gpt-3.5-turbo-1106 ( get more in https://platform.openai.com/docs/models )
                    .messages(messages)
                    .functions(functionExecutor.getFunctions())
                    .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
                    .n(1)
                    .maxTokens(Optional.ofNullable(maxTokens).orElse(1000))
                    .logitBias(new HashMap<>())
                    .build();
            Flowable<ChatCompletionChunk> flowable = service.streamChatCompletion(chatCompletionRequest);

            AtomicBoolean isFirst = new AtomicBoolean(true);
            ChatMessage chatMessage = service.mapStreamToAccumulator(flowable)
                    .doOnNext(accumulator -> {
                        if (accumulator.isFunctionCall() && isFirst.getAndSet(false)) {
                                System.out.println("Executing function " + accumulator.getAccumulatedChatFunctionCall().getName() + "...");
                        } else {
                            if (isFirst.getAndSet(false) && accumulator.getMessageChunk().getContent() != null) {
                                System.out.print("Response: ");
                            }
                            if (accumulator.getMessageChunk().getContent() != null) {
                                System.out.print(accumulator.getMessageChunk().getContent());
                            }
                        }
                    })
                    .doOnComplete(System.out::println)
                    .lastElement()
                    .blockingGet()
                    .getAccumulatedMessage();

            messages.add(chatMessage);

            if (chatMessage.getFunctionCall() != null) {
                System.out.println("Trying to execute " + chatMessage.getFunctionCall().getName() + "...");
                ChatMessage functionResponse = functionExecutor.executeAndConvertToMessageHandlingExceptions(chatMessage.getFunctionCall());
                System.out.println("Executed " + chatMessage.getFunctionCall().getName() + ".");
                messages.add(functionResponse);
            }


    }

}
