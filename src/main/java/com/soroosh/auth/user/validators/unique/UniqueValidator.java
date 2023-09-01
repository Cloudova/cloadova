package com.soroosh.auth.user.validators.unique;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.MessageFormat;

public class UniqueValidator implements ConstraintValidator<Unique, String> {
    @PersistenceContext
    private EntityManager entityManager;

    private Unique u;

    @Override
    public void initialize(Unique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.u = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String queryString = this.u.query();
        Query query;
        if (queryString.isEmpty() || queryString.isBlank()) {
            query = this.entityManager.createQuery(
                    MessageFormat.format("SELECT COUNT(*) from {0} o WHERE o.{1} = :value", this.u.object(), this.u.field()),
                    Long.class).setParameter("value", value);
        } else {
            query = this.entityManager.createQuery(queryString, Long.class).setParameter("value", value);
        }
        return ((Long) query.getSingleResult()) == 0;
    }
}
