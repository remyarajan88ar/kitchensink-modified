package org.quickstarts.kitchensink.rest;

import org.quickstarts.kitchensink.data.MemberRepository;
import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.quickstarts.kitchensink.service.MemberRegistration;

import java.util.*;

@RestController
@RequestMapping("/members")
@Slf4j
public class MemberResourceRESTService {

    @Autowired
    private Validator validator;

    @Autowired
    private MemberRepository repository;

    @Autowired
    private MemberRegistration registration;

    @GetMapping("")
    public List<Member> listAllMembers(){
        List<Member> members= repository.findAllByOrderByNameAsc();
        return members;
    }

    @GetMapping("/{id:[0-9][0-9]*}")
    public Member lookupMemberById(@PathVariable("id") long id){
        Member member = repository.findById(id).orElse(null);
        if(member == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return member;
    }

    @PostMapping("")
    public ResponseEntity createMember(@RequestBody Member member){
        try {
            validateMember(member);
            registration.register(member);
            return ResponseEntity.ok().build();
        }catch (ConstraintViolationException ce){
            return createViolationResponse(ce.getConstraintViolations());
        }catch (ValidationException e){
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObj);
        }catch (Exception e){
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(responseObj);
        }


    }

    private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    private ResponseEntity createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.warn("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return ResponseEntity.badRequest().body(responseObj);
    }

    public boolean emailAlreadyExists(String email) {
        Member member = null;
        try {
            member = repository.findByEmail(email).orElse(null);
        } catch (NoResultException e) {
            // ignore
        }
        return member != null;
    }



}
