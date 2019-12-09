package com.electronicssales.repositories.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.electronicssales.models.ImportInvoiceFetchOption;
import com.electronicssales.models.ImportInvoiceProjections;
import com.electronicssales.models.ProjectionsContants;
import com.electronicssales.repositories.CustomizeImportInvoiceRepository;
import com.electronicssales.utils.SqlDelimiters;
import com.electronicssales.utils.SqlGenerators;
import com.electronicssales.utils.SqlKeyWords;
import com.electronicssales.utils.SqlOperators;

@SuppressWarnings("unchecked")
public class CustomizeImportInvoiceRepositoryImpl implements CustomizeImportInvoiceRepository {
	
	private static final String FETCH_ALL_IMPORT_INVOICE_QUERY = "SELECT " + 
			"	import.id as id," + 
			"	import.import_time as importTime," + 
			"	import.quantity as quantity," + 
			"	u.username as creatorUsername," + 
			"	p.product_name as productName, " + 
			"	(SELECT TOP 1" + 
			"		i.data  " + 
			"	FROM images i" + 
			"		INNER JOIN product_images pImage" + 
			"			ON i.id = pImage.image_id" + 
			"	WHERE pImage.product_id = p.id) as productImage, " + 
			"i1.data as creatorAvartar " +
			"	FROM import_invoices import" + 
			"		INNER JOIN users u" + 
			"			ON import.creator_id = u.id" + 
			"		INNER JOIN products p" + 
			"			ON import.product_id = p.id " +
			"		LEFT JOIN images i1 " + 
			"			ON i1.id = u.avartar_id";
	
	private static final String COUNT_QUERY = "SELECT COUNT(import.id) FROM import_invoices import";
	
	private static final String IMPORT_TIME_COLUMN_NAME_CASTED_TO_DATE = "CAST(import.import_time as DATE)";
	
	private static final String TO_DATE_PARAMETER_NAME = "toDate";
	
	private static final String FROM_DATE_PARAMETER_NAME = "fromDate";
	
	private static final Sort DEFAULT_SORT = Sort.by(Direction.DESC, "importTime");

	private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, DEFAULT_SORT);
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public Page<ImportInvoiceProjections> getImportInvoices(ImportInvoiceFetchOption option, Pageable pageable) {
		StringBuilder fetchAllQuerybuilder = new StringBuilder(FETCH_ALL_IMPORT_INVOICE_QUERY);
		StringBuilder countQueryBuilder = new StringBuilder(COUNT_QUERY);
		Map<String, Object> parameters = new HashMap<String, Object>();
		if(Objects.nonNull(option) && !option.isEmpty()) {
			StringBuilder conditionBuilder = new StringBuilder(SqlKeyWords.WHERE)
					.append(SqlDelimiters.SPACE);
			List<StringBuilder> conditions = new ArrayList<>();
			if(Objects.nonNull(option.getFromDate())) {
				conditions.add(new StringBuilder(IMPORT_TIME_COLUMN_NAME_CASTED_TO_DATE)
						.append(SqlOperators.GREATER_THAN_OR_EQUAL_TO)
						.append(":")
						.append(FROM_DATE_PARAMETER_NAME)
				);
				parameters.put(FROM_DATE_PARAMETER_NAME, option.getFromDate());
			}
			
			if(Objects.nonNull(option.getToDate())) {
				conditions.add(new StringBuilder(IMPORT_TIME_COLUMN_NAME_CASTED_TO_DATE)
						.append(SqlOperators.LESS_THAN_OR_EQUAL_TO)
						.append(":")
						.append(TO_DATE_PARAMETER_NAME)
				);
				parameters.put(TO_DATE_PARAMETER_NAME, option.getToDate());
			}
			conditionBuilder.append(String.join(SqlOperators.AND, conditions));
			fetchAllQuerybuilder.append(SqlDelimiters.SPACE).append(conditionBuilder);
			countQueryBuilder.append(SqlDelimiters.SPACE).append(conditionBuilder);
		}
		
		pageable = Objects.isNull(pageable) ? DEFAULT_PAGEABLE : pageable;
		
		fetchAllQuerybuilder.append(SqlDelimiters.SPACE)
			.append(SqlGenerators.buildOrderBy(pageable.getSort()));
		
		Query fetchAllQuery = entityManager
			.createNativeQuery(
					fetchAllQuerybuilder.toString(), 
					ProjectionsContants.IMPORT_INVOICE_MAPPING_NAME
			);
		
		if(pageable.isPaged()) {
			fetchAllQuery
				.setFirstResult((int)pageable.getOffset())
				.setMaxResults(pageable.getPageSize());
		}
				
		Query countQuery = entityManager
			.createNativeQuery(countQueryBuilder.toString());
		
		parameters.forEach((k, v) -> {
			fetchAllQuery.setParameter(k, v);
			countQuery.setParameter(k, v);
		});
		
		final List<ImportInvoiceProjections> importInvoices = fetchAllQuery.getResultList();
		final int totalElements = (int) countQuery.getSingleResult();
		return new PageImpl<>(importInvoices, pageable, totalElements);
	}

}
