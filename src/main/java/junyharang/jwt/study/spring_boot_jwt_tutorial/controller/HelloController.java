package junyharang.jwt.study.spring_boot_jwt_tutorial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api") public class HelloController {

    @GetMapping("/hello") public ResponseEntity<String> hello() { // hello 문자열을 return 하는 Method
        return ResponseEntity.ok("hello");
    } // hello() 끝

} // class 끝
