package com.gitee.freakchicken.dbapi.gateway.controller;

import com.gitee.freakchicken.dbapi.basic.service.IPService;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metacache")
@Slf4j
public class MetaCacheController {

    @Autowired
    IPService ipService;

    @RequestMapping("/iprule/sync")
    public ResponseDto hello(){
        ipService.init();
        return ResponseDto.successWithData(null);
    }
}
