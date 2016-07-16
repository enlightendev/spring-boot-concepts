package io.enlightendev.controllers;

import io.enlightendev.domain.Greeting;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This code uses Spring 4’s new @RestController annotation, which marks the
 * class as a controller where every method returns a domain object instead
 * of a view. It’s shorthand for @Controller and @ResponseBody rolled together.
 */
@RestController
@RequestMapping("/api")
public class MainController {

    private final Logger log = LogManager.getLogger(this.getClass());

    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();

    /**
     * Request params are appended to url
     * @param name
     * @return
     */
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="world") String name) {

        log.debug("greeting( name: {} )" + name);

        return new Greeting(counter.incrementAndGet(), String.format(template, name));

    }

    /**
     *
     * @param userAgent
     * @param clientId
     * @return
     */
    @RequestMapping("/requestHeaders")
    public @ResponseBody ResponseEntity<Greeting> requestHeaders (
            @RequestHeader(value="user-agent", defaultValue="foo") String userAgent,
            @RequestHeader(value="client-id", defaultValue="1234") String clientId ){

        log.debug("requestHeaders( userAgent: " + userAgent + " clientId: " + clientId +")" );

        Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, "world"));

        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);

    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "responseHeaders", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Greeting> responseHeaders(){

        Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, "world"));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("RequestID", "aaa-123");
        responseHeaders.set("ResponseID", "res-8765");

        return new ResponseEntity<Greeting>(greeting, responseHeaders, HttpStatus.OK);

    }

    /**
     *
     * @param jsonString
     * @return
     */
    @RequestMapping(value = "jsonBody", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Greeting> jsonBody(@RequestBody String jsonString){

        JSONObject jsonObject = new JSONObject(jsonString);

        Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, "world"));

        JSONObject user = (JSONObject)jsonObject.get("user");
        String password = (String)user.get("password");

        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);

    }

}