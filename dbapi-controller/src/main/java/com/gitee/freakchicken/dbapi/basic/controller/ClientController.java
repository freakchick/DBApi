package com.gitee.freakchicken.dbapi.basic.controller;

import com.gitee.freakchicken.dbapi.basic.domain.Client;
import com.gitee.freakchicken.dbapi.basic.service.ClientService;
import com.gitee.freakchicken.dbapi.basic.util.ThreadContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * 创建应用
     *
     * @param client
     * @return
     */
    @PostMapping("/create")
    public Client create(Client client) {
        client.setCreateUserId(ThreadContainer.userThreadLocal.get().getId());
        client = clientService.add(client);
        return client;
    }

    @PostMapping("/getAll")
    public List<Client> getAll() {
        List<Client> list = clientService.getAll();
        return list;
    }

    @PostMapping("/delete/{clientId}")
    public void delete(@PathVariable("clientId") String clientId) {
        clientService.delete(clientId);
    }

    @PostMapping("/auth")
    public void auth(String clientId, String groupIds) {
        clientService.auth(clientId, groupIds);
    }

    @PostMapping("/getAuthGroups/{clientId}")
    public List<String> getAuthGroups(@PathVariable("clientId") String clientId) {
        return clientService.getAuthGroups(clientId);
    }
}