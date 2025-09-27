package com.application.BankStatement.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.application.BankStatement.entity.Statement;
import com.application.BankStatement.entity.User;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.application.BankStatement.services.BankServices;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("bank")
public class StatementController {

    @Autowired
    BankServices bankServices;

    @PostMapping("statement")
    public ResponseEntity<?> getFile(@RequestPart User user, @RequestPart MultipartFile file) {
        try {
            User saveuser = bankServices.addUser(user, file);
            return new ResponseEntity<>(saveuser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

   @GetMapping("readFile/{id}")
    public ResponseEntity<List<Statement>> fileData(@PathVariable int id) throws IOException {
            List<Statement> Content = bankServices.readFile(id);
            return new ResponseEntity<>(Content, HttpStatus.OK);
   }

    @GetMapping("readFile/{name}")
    public ResponseEntity<List<Statement>> fileDataName(@PathVariable String name) throws IOException {
        List<Statement> Content = bankServices.findAccountBy(name);
        return new ResponseEntity<>(Content, HttpStatus.OK);
    }
}
