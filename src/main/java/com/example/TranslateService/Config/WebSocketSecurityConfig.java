/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 *
 * @author Artur
 */

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer{
    
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages.simpDestMatchers("/app/**").authenticated().anyMessage().authenticated()
//        .simpSubscribeDestMatchers("/topic/**").authenticated().anyMessage().authenticated();
        messages.simpDestMatchers("/app/**").permitAll().anyMessage().permitAll()
        .simpSubscribeDestMatchers("/topic/**").permitAll().anyMessage().permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
    
}
