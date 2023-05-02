package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    @Autowired
    JdbcTransferDao transferDao;
    @Autowired
    JdbcUserDao userDao;

    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public double getBalance(Principal principal) {
        return transferDao.getBalance(principal.getName());
    }

    @RequestMapping(path = "send", method = RequestMethod.POST)
    public Transfer sendMoney(Principal principal, @RequestBody Transfer transfer) {
        List<String> usernameList = userDao.findAll().stream().map(User::getUsername).collect(Collectors.toList());
        if (principal.getName().equals(transfer.getUsernameTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't send money to yourself.");
        }
        if (!usernameList.contains(transfer.getUsernameTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist.");
        }
        if (transferDao.getBalance(principal.getName()) < transfer.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough balance.");
        }
        return transferDao.sendMoney(principal.getName(), transfer);
    }
    @RequestMapping(path = "transfers", method = RequestMethod.GET)
    public List<Transfer> transferList(Principal principal){
        return transferDao.seeTransfer(principal.getName());
    }
    @RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(Principal principal, @PathVariable int id){
        Transfer transfer = transferDao.findTransferByTransferId(principal.getName(), id);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No access to transfer/ Transfer doesn't exist.");
        } else {
            return transfer;
        }
    }
    @RequestMapping(path = "request", method = RequestMethod.POST)
    public Transfer requestMoney(Principal principal, @RequestBody Transfer transfer) {
        List<String> usernameList = userDao.findAll().stream().map(User::getUsername).collect(Collectors.toList());
        if (principal.getName().equals(transfer.getUsernameFrom())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't request money from yourself.");
        }
        if (!usernameList.contains(transfer.getUsernameFrom())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist.");
        }
        return transferDao.requestMoney(principal.getName(), transfer);
    }
    @RequestMapping(path = "transfers/pending", method = RequestMethod.GET)
    public List<Transfer> seePending(Principal principal) {
        return transferDao.seePending(principal.getName());
    }
    @RequestMapping(path = "approve/{id}/{status}", method = RequestMethod.PUT)
    public Transfer approveRequest(Principal principal, @PathVariable int id, @PathVariable String status){
        Transfer transfer = transferDao.findTransferByTransferId(principal.getName(), id);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No access to transfer/ Transfer doesn't exist.");
        }
        if (status.equals("approve")) {
            status = "Approved";
        } else if (status.equals("reject")) {
            status = "Rejected";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect input.");
        }
        if (transferDao.getBalance(principal.getName()) < transfer.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds.");
        }
        return transferDao.approveRequest(principal.getName(), id, status);
    }
}
