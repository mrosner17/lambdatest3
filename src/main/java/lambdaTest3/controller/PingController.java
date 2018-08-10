package lambdaTest3.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import lambdaTest3.StreamLambdaHandler;

import java.util.HashMap;
import java.util.Map;


@RestController
@EnableWebMvc
public class PingController {
	private static Logger log = LoggerFactory.getLogger("lambdaTest3");
    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    public Map<String, String> ping() {
        Map<String, String> pong = new HashMap<>();
        
        System.out.println("CONTROLLER - This is loging from System.out");
        log.info("CONTROLLER - This is logging from slf4j");
        log.warn("CONTROLLER - This is logging from slf4j");
        log.error("CONTROLLER - This is logging from slf4j");
        
        pong.put("pong", "Hello, World!");
        return pong;
    }
}
