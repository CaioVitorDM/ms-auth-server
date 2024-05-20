package ufrn.imd.br.msauthserver.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ufrn.imd.br.msauthserver.model.User;

import java.util.Collections;
import java.util.List;

@Transactional
public class CustomUserRepositoryImpl implements CustomUserRepository{

    @PersistenceContext
    private EntityManager entityManager;

    private static final String INITIAL = "SELECT u FROM  User u " +
            "LEFT JOIN Patient p ON p.id = u.patientId AND p.active = TRUE ";

    private static final String WHERE = "WHERE 1=1 AND u.role = 'PATIENT' ";

    @Override
    public Page<User> searchByFilters(String name, String email, String phoneNumber, String doctorId, Pageable pageable) {
        StringBuilder whereClause = new StringBuilder(WHERE);
        String orderField = "createAt";
        String orderDirection = "DESC";

        if (!pageable.getSort().isUnsorted()) {
            orderField = pageable.getSort().get().iterator().next().getProperty();
            orderDirection = pageable.getSort().get().iterator().next().getDirection().name();
        }

        if (name != null && !name.trim().isEmpty()) {
            whereClause.append("AND LOWER(p.name) LIKE LOWER(:name) ");
        }
        if (email != null && !email.trim().isEmpty()) {
            whereClause.append("AND LOWER(u.email) LIKE LOWER(:email) ");
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            whereClause.append("AND u.phoneNumber = :phoneNumber ");
        }
        if (doctorId != null && !doctorId.trim().isEmpty()) {
            whereClause.append("AND p.doctorId = :doctorId ");
        }

        String countQueryStr = "SELECT COUNT(u) FROM User u " +
                "LEFT JOIN Patient p ON p.id = u.patientId " + whereClause;
        Query countQuery = entityManager.createQuery(countQueryStr);
        setQueryParameters(countQuery, name, email, phoneNumber, doctorId);

        long count = ((Number) countQuery.getSingleResult()).longValue();
        if (count == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, count);
        }

        // Construção da consulta final com paginação
        String finalQuery = INITIAL + whereClause + " ORDER BY u." + orderField + " " + orderDirection;
        Query query = entityManager.createQuery(finalQuery, User.class);
        setQueryParameters(query, name, email, phoneNumber, doctorId);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<User> resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }

    private void setQueryParameters(Query query, String name, String email, String phoneNumber, String doctorId) {
        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (email != null && !email.trim().isEmpty()) {
            query.setParameter("email", "%" + email + "%");
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            query.setParameter("phoneNumber", phoneNumber);
        }
        if (doctorId != null && !doctorId.trim().isEmpty()) {
            query.setParameter("doctorId", doctorId);
            System.out.println("QUERY DOCTOR ID: " + doctorId);
        }
    }

    public List<User> searchByFilters(String doctorId, String login, String name) {
        StringBuilder whereClause = new StringBuilder(WHERE);
        String orderField = "createdAt";
        String orderDirection = "DESC";

        if (name != null && !name.trim().isEmpty()) {
            whereClause.append("AND LOWER(p.name) LIKE LOWER(:name) ");
        }
        if (login != null && !login.trim().isEmpty()) {
            whereClause.append("AND LOWER(u.login) LIKE LOWER(:login) ");
        }

        if (doctorId != null && !doctorId.trim().isEmpty()) {
            whereClause.append("AND p.doctorId = :doctorId ");
        }

        // Construção da consulta final sem paginação
        String finalQuery = INITIAL + whereClause + " ORDER BY u." + orderField + " " + orderDirection;
        Query query = entityManager.createQuery(finalQuery, User.class);
        setQueryParameters(query, name, login, doctorId);

        return (List<User>) query.getResultList();
    }

    private void setQueryParameters(Query query, String name, String login, String doctorId) {
        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
        }
        if (login != null && !login.trim().isEmpty()) {
            query.setParameter("login", "%" + login.toLowerCase() + "%");
        }
        if (doctorId != null && !doctorId.trim().isEmpty()) {
            query.setParameter("doctorId", doctorId);
        }
    }


}
