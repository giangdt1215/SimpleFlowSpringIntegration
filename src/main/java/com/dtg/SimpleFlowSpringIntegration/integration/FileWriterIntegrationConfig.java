package com.dtg.SimpleFlowSpringIntegration.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
@IntegrationComponentScan
@EnableIntegration
public class FileWriterIntegrationConfig {

    @Bean
    public MessageChannel textInChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fileWriterChannel(){
        return new DirectChannel();
    }

    @Bean
    @Transformer(inputChannel = "textInChannel",
                 outputChannel = "fileWriterChannel")
    public GenericTransformer<String, String> upperCaseTransformer(){
        return text -> text.toUpperCase();
    }

    @Bean
    @ServiceActivator(inputChannel = "fileWriterChannel")
    public FileWritingMessageHandler fileWriter(){
        File file = new File("D:\\Workspace\\Intellij\\SimpleFlowSpringIntegration\\tmp");
        FileWritingMessageHandler handler =
                new FileWritingMessageHandler(file);
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        return handler;
    }

    @Bean
    public IntegrationFlow fileWriterFlow(){
        return IntegrationFlows
                .from(MessageChannels.direct("textInChannelDsl"))
                .<String, String>transform(t -> t.toUpperCase())
                .channel(MessageChannels.direct("FileWriterChannelDsl"))
                .handle(Files
                        .outboundAdapter(
                                new File("D:\\Workspace\\Intellij\\SimpleFlowSpringIntegration\\tmp\\dsl"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true))
                .get();
    }
}
