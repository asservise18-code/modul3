package com.example.modul3.specification;

import com.example.modul3.model.Client;
import org.springframework.data.jpa.domain.Specification;


public class ClientSpecification {

    public static Specification<Client> firstNameContains(String firstName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("firstName")),
                        "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<Client> lastNameContains(String lastName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("lastName")),
                        "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Client> emailContains(String email) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%");
    }

    public static Specification<Client> phoneContains(String phone) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("phone")),
                        "%" + phone.toLowerCase() + "%");
    }

}
