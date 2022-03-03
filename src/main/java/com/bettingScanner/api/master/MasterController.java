package com.bettingScanner.api.master;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bettingScanner.api.services.SlaveService;
import com.bettingScanner.api.tipsport.Match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

@RestController
@RequestMapping("/master/v1")
public class MasterController {

    @Autowired
    private SlavesRepository slavesRepository;

    @Autowired
    private SlaveService slaveService;

    @GetMapping("/getSlaves")
    public List<Slave> getSlaves() {
        return slavesRepository.findAll().stream().sorted(Comparator.comparing(Slave::getLastUsed).reversed())
                .collect(Collectors.toList());
    }

    @PostMapping("testSlave")
    public SlaveTestResult testSlave(@RequestParam String slaveAddress) throws NotFoundException {
        Slave slave = slavesRepository.findById(slaveAddress).orElse(null);
        if (slave == null)
            throw new NotFoundException("Slave with address " + slaveAddress + " not found.");
        return slaveService.testSlave(slave);
    }

    @PostMapping("/changeEnabled")
    public void changeEnabled(@RequestParam String slaveAddress, @RequestParam Boolean enabled)
            throws NotFoundException {
        Slave slave = slavesRepository.findById(slaveAddress).orElse(null);
        if (slave == null)
            throw new NotFoundException("Slave with address " + slaveAddress + " not found.");
        slave.setEnabled(enabled);
        slavesRepository.saveAndFlush(slave);
    }

    @PostMapping("/scann")
    public void masterScann() {
        Slave currentSlave = selectSlave(slavesRepository.findAllByEnabled(true));
        currentSlave.setLastUsed(LocalDateTime.now());
        slavesRepository.saveAndFlush(currentSlave);
        slaveService.slaveScann(currentSlave);
    }

    @GetMapping("/matches")
    public List<Match> getMatchesFromSlave(@RequestParam String url,
            @RequestParam(defaultValue = "COMPETITION") String categoryType) {
        Slave currentSlave = selectSlave(slavesRepository.findAllByEnabled(true));
        currentSlave.setLastUsed(LocalDateTime.now());
        slavesRepository.saveAndFlush(currentSlave);
        return slaveService.slaveSacannMatches(currentSlave, url, categoryType);
    }

    private Slave selectSlave(List<Slave> activeSlaves) {
        Optional<Slave> todayActive = activeSlaves.stream()
                .filter(s -> s.getLastUsed() != null && s.getLastUsed().toLocalDate().equals(LocalDate.now()))
                .findAny();
        if (todayActive.isPresent())
            return todayActive.get();
        Optional<Slave> notScanned = activeSlaves.stream().filter(s -> s.getLastUsed() == null).findFirst();
        if (notScanned.isPresent())
            return notScanned.get();
        return activeSlaves.stream().sorted(Comparator.comparing(Slave::getLastUsed)).findFirst().orElse(null);
    }

}
