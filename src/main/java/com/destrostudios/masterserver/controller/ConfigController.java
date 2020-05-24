package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.database.ClientConfigRepository;
import com.destrostudios.masterserver.database.schema.ClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ClientConfigRepository clientConfigRepository;

    @GetMapping("/client")
    public Map<String, String> getAllClientConfigs() {
        List<ClientConfig> clientConfigs = clientConfigRepository.findAll();
        return clientConfigs.stream().collect(Collectors.toMap(ClientConfig::getName, ClientConfig::getValue));
    }
}
