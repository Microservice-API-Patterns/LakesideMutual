package com.lakesidemutual.customercore.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This controller demonstrates the use of a 301 Moved Permanently response to redirect requests to a new URI.
 *
 * See the <a href="https://interface-refactoring.github.io/refactorings/renameendpoint">Rename Endpoint</a> refactoring for more information.
 */
@RestController
@RequestMapping(path = "getCustomers")
public class OldCustomerInformationHolder {

    /**
     * Handles GET requests for customer information.
     *
     * @param ids    The customer IDs.
     * @param fields The fields to be included in the response (optional).
     * @return A ResponseEntity with a status of 301 Moved Permanently and the location header set to the new URI.
     */
    @GetMapping(value = "/{ids}")
    @ResponseBody
    public ResponseEntity getCustomer(
            @PathVariable String ids,
            @RequestParam(value = "fields", required = false, defaultValue = "") String fields) {
        URI movedTo = linkTo(methodOn(CustomerInformationHolder.class).getCustomer(ids, fields)).toUri();
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(movedTo)
                .build();
    }
}