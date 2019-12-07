package com.electronicssales.repositories.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.electronicssales.models.RevenueStatisticalProjections;
import com.electronicssales.models.ProjectionsContants;
import com.electronicssales.models.StatisticalType;
import com.electronicssales.repositories.RevenueStatisticalRepository;
import com.electronicssales.utils.SqlGenerators;

@SuppressWarnings("unchecked")
public class RevenueStatisticalRepositoryImpl implements RevenueStatisticalRepository {
	
	private static final int DEFAULT_PAGE_INDEX = 0;
	
	private static final int DEFAULT_PAGE_SIZE = 20;
	
	private static final Direction DEFAULT_SORT_DIRECTION = Direction.DESC;
	
	private static final Sort MONTH_SORT_DESC = Sort.by(DEFAULT_SORT_DIRECTION, "month");
	
	private static final Sort YEAR_SORT_DESC = Sort.by(DEFAULT_SORT_DIRECTION, "year");
	
	private static final Sort DAY_SORT_DESC = Sort.by(DEFAULT_SORT_DIRECTION, "day");
	
	private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE);
	
	private static final String SPACE = " ";
	
	private static final String COMMA_AND_SPACE_DELIMITER = "," + SPACE;
	
	private static final String SELECT_KEYWORD = "SELECT";
	
	private static final String GROUP_KEYWORD = "GROUP BY";
	
	private static final List<String> DEFAULT_FIELD_SELECT_QUERY = Arrays.asList(
			"YEAR(created_time) as 'year'", 
			"MAX((td.price - (CASE td.discount_type " + 
					"WHEN 'PERCENT' THEN (td.price * td.discount_value / 100) " + 
					"WHEN 'AMOUNT' THEN td.discount_value " + 
					"ELSE 0 " + 
					"END)) * td.quantity) as maxRevenue",
			"MIN((td.price - (CASE td.discount_type " + 
					"WHEN 'PERCENT' THEN (td.price * td.discount_value / 100) " + 
					"WHEN 'AMOUNT' THEN td.discount_value " + 
					"ELSE 0 " + 
					"END)) * td.quantity) as minRevenue",
			"SUM((td.price - (CASE td.discount_type " + 
					"WHEN 'PERCENT' THEN (td.price * td.discount_value / 100) " + 
					"WHEN 'AMOUNT' THEN td.discount_value " + 
					"ELSE 0 " + 
					"END)) * td.quantity) as totalRevenue"			
	);
	
	private static final String YEAR_FIELD_GROUP_NAME = "YEAR(created_time)";
	
	private static final String MONTH_FIELD_GROUP_NAME = "MONTH(created_time)";
	
	private static final String DAY_FIELD_GROUP_NAME = "DAY(created_time)";
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public <T extends RevenueStatisticalProjections> List<T> getRevenueStatistical(StatisticalType statisticalType,
			Pageable pageable) {
		StringBuilder queryBuilder = new StringBuilder(getFullReveneStatisticalQuery(statisticalType));
		
		pageable = pageable == null ? DEFAULT_PAGEABLE : pageable;
		
		Sort sort = Optional.of(pageable.getSort())
			.filter(Sort::isSorted)
			.orElse(getSortBy(statisticalType));
		
		queryBuilder.append(SPACE)
			.append(SqlGenerators.buildOrderBy(sort));
		
		String sqlResultsetMappingName = getRevenueStatisticalMapping(statisticalType);
		return entityManager.createNativeQuery(
					queryBuilder.toString(), 
					sqlResultsetMappingName
				)
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
	}
	
	private String getFullReveneStatisticalQuery(StatisticalType statisticalType) {
		return new StringBuilder(getRevenueStatisticalSelectStatement(statisticalType))
				.append(SPACE)
				.append(createGroup(statisticalType))
				.toString();
	}
	
	private String getRevenueStatisticalMapping(StatisticalType statisticalType) {
		switch (Objects.requireNonNull(statisticalType)) {
			case YEAR:
				return ProjectionsContants.REVENUE_STATISTICAL_MAPPING_NAME;
			case MONTH:
				return ProjectionsContants.REVENUE_BY_MONTH_STATISTICAL_MAPPING_NAME;
			default:
				return ProjectionsContants.REVENUE_BY_DAY_STATISTICAL_MAPPING_NAME;
		}
	}
	
	private String getRevenueStatisticalSelectStatement(StatisticalType statisticalType) {
		if(statisticalType != StatisticalType.MONTH && statisticalType != StatisticalType.DAY) {
			return createSelectStatement(DEFAULT_FIELD_SELECT_QUERY);
		}
		
		List<String> fieldsSelect = new ArrayList<>();
		
		if(statisticalType == StatisticalType.DAY) {
			fieldsSelect.add("DAY(created_time) as 'day'");
		}
		
		fieldsSelect.add("MONTH(created_time) as 'month'");
		
		DEFAULT_FIELD_SELECT_QUERY.forEach(fieldsSelect::add);
		
		return createSelectStatement(fieldsSelect);
	}
	
	private String createSelectStatement(String fieldsSelect) {
		return new StringBuilder(SELECT_KEYWORD)
					.append(SPACE)
					.append(fieldsSelect)
					.append(" FROM transactions t")
					.append(" INNER JOIN transaction_detaileds td")
					.append(" ON t.id = td.transaction_id")
					.toString();
	}
	
	private String createSelectStatement(List<String> fieldsSelect) {
		return createSelectStatement(
				fieldsSelect.stream().collect(Collectors.joining(COMMA_AND_SPACE_DELIMITER))
		);
	}
	
	private String createGroup(StatisticalType statisticalType) {
		StringBuilder builder = new StringBuilder(GROUP_KEYWORD);
		builder.append(SPACE)
				.append(YEAR_FIELD_GROUP_NAME);
		if(statisticalType == StatisticalType.MONTH || statisticalType == StatisticalType.DAY) {
			builder
				.append(COMMA_AND_SPACE_DELIMITER)
				.append(MONTH_FIELD_GROUP_NAME);
		}
		
		if(statisticalType == StatisticalType.DAY) {
			builder
				.append(COMMA_AND_SPACE_DELIMITER)
				.append(DAY_FIELD_GROUP_NAME);
		}
		return builder.toString();
	}
	
	private Sort getSortBy(StatisticalType statisticalType) {
		Sort sort  = SerializationUtils.clone(YEAR_SORT_DESC);
		
		if(statisticalType == StatisticalType.MONTH || statisticalType == StatisticalType.DAY) {
			sort = sort.and(MONTH_SORT_DESC);
		}
		
		if(statisticalType == StatisticalType.DAY) {
			sort = sort.and(DAY_SORT_DESC);
		}
		
		return sort;
	}
	
}
