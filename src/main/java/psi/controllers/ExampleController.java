package psi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/example")
public class ExampleController {

  @GetMapping
  public String getTestData() {
    return "Hello there";
  }
}