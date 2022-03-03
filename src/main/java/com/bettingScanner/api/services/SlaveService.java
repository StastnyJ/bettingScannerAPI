package com.bettingScanner.api.services;

import java.util.List;

import com.bettingScanner.api.master.Slave;
import com.bettingScanner.api.master.SlaveTestResult;
import com.bettingScanner.api.tipsport.Match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SlaveService {

    @Autowired
    private RestTemplate rest;

    public void slaveScann(Slave slave) {
        rest.postForObject(String.format("http://%s:%d/requests/v1/scan", slave.getIpAddress(), slave.getPort()), null,
                Void.class);
    }

    @SuppressWarnings("unchecked")
    public List<Match> slaveSacannMatches(Slave slave, String url, String categoryType) {
        return rest.getForObject(String.format("http://%s:%d/tipsport/v1/matches?url=%s&categoryType=%s",
                slave.getIpAddress(), slave.getPort(), url, categoryType), List.class);
    }

    public SlaveTestResult testSlave(Slave slave) {
        try {
            String token = rest.getForObject(String.format("http://%s:%d/tipsport/v1/token",
                    slave.getIpAddress(), slave.getPort()), String.class);
            return new SlaveTestResult(true, token != null && token.length() > 0);
        } catch (Exception ex) {
            return new SlaveTestResult(false, false);
        }
    }
}
