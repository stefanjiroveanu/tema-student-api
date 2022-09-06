package com.nagarro.studentapi.configuration;

import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.integration.queue.StudentSender;
import com.nagarro.studentapi.util.XmlParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.handler.advice.AbstractRequestHandlerAdvice;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
@EnableIntegration
public class IntegrationConfiguration {

    private static final String XML = "*.xml";
    private static final String STUDENT_BUN_XML = "\\student_bun.xml";
    private static final String STUDENT_RAU_XML = "\\student_rau.xml";
    private static final String ERROR = "Error";
    private final StudentSender studentSender;

    @Value("${student-api.xmlPath}")
    private String inputPath;
    @Value("${student-api.archivedDestination}")
    private String successPath;
    @Value("${student-api.errorDestination}")
    private String errorPath;

    public IntegrationConfiguration(StudentSender studentSender) {
        this.studentSender = studentSender;
    }

    @Bean
    public MessageChannel messageChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "messageChannel")
    public MessageSource<File> messageProducer() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(inputPath));
        messageSource.setFilter(new SimplePatternFileListFilter(XML));
        return messageSource;
    }

    @Bean
    @ServiceActivator(inputChannel = "messageChannel")
    public MessageHandler handler() {
        return message -> {
            if (message.getPayload() instanceof Student) {
                studentSender.send((Student) message.getPayload());
            }
        };
    }

    @Bean
    public IntegrationFlow integrationFlow(XmlParser xmlParser) {
        return IntegrationFlows.from(messageProducer())
                .enrichHeaders(h -> h.headerExpression(FileHeaders.ORIGINAL_FILE, "payload"))
                .convert(String.class)
                .transform((String path) -> xmlParser.parsePath(path), e -> e.advice(errorAdvice()))
                .handle("handler", "handleMessage")
                .get();
    }

    @Bean
    public AbstractRequestHandlerAdvice errorAdvice() {
        return new AbstractRequestHandlerAdvice() {

            @Override
            protected Object doInvoke(ExecutionCallback callback, Object target, Message<?> message) {
                File file = message.getHeaders().get(FileHeaders.ORIGINAL_FILE, File.class);
                try {
                    Object result = callback.execute();
                    file.renameTo(new File(successPath, STUDENT_BUN_XML));
                    System.out.println("File renamed after success");
                    return result;
                } catch (Exception e) {
                    file.renameTo(new File(errorPath, STUDENT_RAU_XML));
                    System.out.println("File renamed after failure");
                    return ERROR;
                }
            }
        };
    }
}
