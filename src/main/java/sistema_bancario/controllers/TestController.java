package sistema_bancario.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sistema_bancario.services.TestService;

@RequestMapping("test")
@RestController
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("ping")
    public ResponseEntity<String> ping() {
        log.info("Ping Acionado!");
        return ResponseEntity.ok(testService.ping());
    }
}
