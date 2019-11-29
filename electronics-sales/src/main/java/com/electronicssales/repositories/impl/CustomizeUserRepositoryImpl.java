package com.electronicssales.repositories.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.electronicssales.models.UserProjections;
import com.electronicssales.repositories.CustomizeUserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import one.util.streamex.EntryStream;

public class CustomizeUserRepositoryImpl implements CustomizeUserRepository {

    private static final String BASE_FETCH_ALL_EMPLOYEE_QUERY = "SELECT u.id AS id,"
            + " ui.lastname AS lastname, ui.firstname AS firstname, u.username AS username,"
            + "ui.gender AS gender, ui.birthday as birthday, ui.email as email,"
            + "ui.phone_number as phoneNumber, ui.address AS address, i.data as avartar  FROM users u INNER JOIN user_infos ui"
            + " ON u.user_info_id = ui.id LEFT JOIN images i ON i.id = u.avartar_id" 
            + " WHERE u.actived = 1 AND u.role = 'EMPLOYEE'";

    private static final String COUNT_EMPLOYEE_QUERY = "SELECT COUNT(u.id) FROM users u"
            + " INNER JOIN user_infos ui ON u.user_info_id = ui.id WHERE u.actived = 1 AND u.role = 'EMPLOYEE'";

    private static final String[] COLUMNS_SEARCH = { "u.id", "ui.lastname", "ui.firstname", "u.username", "ui.email",
            "ui.phone_number", "ui.address" };

    // private static final

    private static final String USER_PROJECTIONS_MAPPING = "UserProjectionsMapping";

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "id");

    private static final int DEFAULT_PAGE_INDEX = 0;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

    private final EntityManager entityManager;

    public CustomizeUserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Page<UserProjections> fetchAllEmployees(String searchKey, Pageable pageable) {
        if(pageable == null) {
            pageable = DEFAULT_PAGEABLE;
        }
        StringBuilder fetchEmployeeSqlBuilder = new StringBuilder(BASE_FETCH_ALL_EMPLOYEE_QUERY);
        StringBuilder countEmployeeSqlBuilder = new StringBuilder(COUNT_EMPLOYEE_QUERY);
        Map<Integer, Object> params = new HashMap<Integer, Object>();
        if (StringUtils.hasText(searchKey)) {
            StringBuilder conditionBuilded = new StringBuilder(" (");
            String conditions = EntryStream.of(getSearchConditions())
                .peekKeys((index) -> params.put(index + 1, searchKey))
                .values()
                .joining(" OR ");
            conditionBuilded.append(conditions);    
            conditionBuilded.append(") ");
            fetchEmployeeSqlBuilder.append(" AND ").append(conditionBuilded);
            countEmployeeSqlBuilder.append(" AND ").append(conditionBuilded);
        }

        // order by
        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : DEFAULT_SORT;
        String orders = sort.get()
            .map(order -> String.join(" ", order.getProperty(), order.getDirection().name()))
            .collect(Collectors.joining(", "));
        fetchEmployeeSqlBuilder.append(" ORDER BY ").append(orders);

        Query fetchEmployeeQuery = entityManager.createNativeQuery(fetchEmployeeSqlBuilder.toString(), USER_PROJECTIONS_MAPPING);
        Query countQuery = entityManager.createNativeQuery(countEmployeeSqlBuilder.toString());

        if(pageable.isPaged()) {
            fetchEmployeeQuery.setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        }

        params.forEach(fetchEmployeeQuery::setParameter);
        params.forEach(countQuery::setParameter);

        final List<UserProjections> employees = fetchEmployeeQuery.getResultList();
        final int totalElements = (int) countQuery.getSingleResult();
        return new PageImpl<>(employees, pageable, totalElements);
    }

    private List<String> getSearchConditions() {
        return EntryStream.of(COLUMNS_SEARCH).mapKeys(index -> index + 1).mapKeyValue((index, column) -> String
                .join(" ", column, String.join(" ", "LIKE", "CONCAT('%',?".concat(index.toString()).concat(",'%')"))))
                .toList();
    }
}