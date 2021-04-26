package dev.nullzwo.user.app.controller;

import de.otto.edison.hal.HalRepresentation;
import de.otto.edison.hal.Link;
import de.otto.edison.hal.Links;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.nullzwo.user.app.controller.ChangeController.CHANGES_PATH;
import static dev.nullzwo.user.app.controller.UserController.USER_PATH;

@RestController
@CrossOrigin
@RequestMapping(produces = {"application/hal+json", "application/json"})
public class IndexController {

    @GetMapping
    public HalRepresentation index() {
        return new HalRepresentation(Links.linkingTo().single(
                Link.link("users", USER_PATH),
                Link.link("changes", CHANGES_PATH)
        ).build());
    }
}
