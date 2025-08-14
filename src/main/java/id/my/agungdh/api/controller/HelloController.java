/* (C) 2025 Agung DH */
package id.my.agungdh.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HelloController {
  @GetMapping
  public String hello() {
    return "Surimbim, dududuuw...";
  }
}
