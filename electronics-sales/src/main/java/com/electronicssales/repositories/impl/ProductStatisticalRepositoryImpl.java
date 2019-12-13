package com.electronicssales.repositories.impl;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.electronicssales.models.ProductStatisticalProjections;
import com.electronicssales.models.ProjectionsContants;
import com.electronicssales.repositories.ProductStatisticalRepository;
import com.electronicssales.utils.SqlDelimiters;
import com.electronicssales.utils.SqlGenerators;
import com.electronicssales.utils.SqlKeyWords;

@SuppressWarnings("unchecked")
public class ProductStatisticalRepositoryImpl implements ProductStatisticalRepository {
	
	private static final List<String> FIELD_SELECT = Arrays.asList(
			"(SELECT TOP 1 \r\n" + 
			"		i.data \r\n" + 
			"	FROM images i \r\n" + 
			"		INNER JOIN product_images pImage\r\n" + 
			"			ON i.id = pImage.image_id\r\n" + 
			"	\r\n" + 
			"	WHERE pImage.product_id = p.id) as 'image'",
			"p.id as id",
			"p.product_name as productName",
			"(SELECT SUM(ii.quantity) FROM import_invoices ii WHERE ii.product_id = p.id) as quantityImport",
			"SUM(td.quantity) as quantitySold",
			"p.quantity as quantityRemaining",
			"SUM(CASE td.discount_type\r\n" + 
			"       WHEN 'PERCENT' THEN (td.price * td.discount_value / 100)\r\n" + 
			"       WHEN 'AMOUNT' THEN td.discount_value\r\n" + 
			"        ELSE 0\r\n" + 
			"       END) as totalDiscount",
			"SUM((td.price - (CASE td.discount_type\r\n" + 
			"     WHEN 'PERCENT' THEN (td.price * td.discount_value / 100)\r\n" + 
			"      WHEN 'AMOUNT' THEN td.discount_value\r\n" + 
			"      ELSE 0\r\n" + 
			"     END)) * td.quantity) as totalRevenue"
	);
	
	private static final String COUNT_QUERY = "SELECT COUNT(DISTINCT p.id) FROM products p INNER JOIN transaction_detaileds td"
			+ " ON td.product_id = p.id INNER JOIN transactions t ON td.transaction_id = t.id";
	
	private static final List<String> GROUP_FIELD = Arrays.asList(
			"p.id", "p.product_name", "p.quantity"
	);

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Page<ProductStatisticalProjections> getProductStatistical(Pageable pageable) {
		pageable = pageable == null ? Pageable.unpaged() : pageable;
		StringBuilder fetchQueryBuilder = new StringBuilder(SqlKeyWords.SELECT)
				.append(SqlDelimiters.SPACE)
				.append(String.join(SqlDelimiters.COMMA_SPACE, FIELD_SELECT))
				.append(SqlDelimiters.SPACE)
				.append(SqlKeyWords.FROM)
				.append(" products p INNER JOIN transaction_detaileds td ")
				.append("ON td.product_id = p.id " + 
						"INNER JOIN " + 
						"transactions t " + 
						"ON td.transaction_id = t.id")
				.append(SqlDelimiters.SPACE);
		
		StringBuilder countQueryBuilder = new StringBuilder(COUNT_QUERY);

//		group
		fetchQueryBuilder.append(SqlKeyWords.GROUP_BY)
			.append(SqlDelimiters.SPACE)
			.append(String.join(SqlDelimiters.COMMA_SPACE, GROUP_FIELD))
			.append(SqlDelimiters.SPACE);
		
//		order by
		fetchQueryBuilder.append(SqlGenerators.buildOrderBy(pageable.getSort()));
		
		
		Query fetchQuery = entityManager.createNativeQuery(fetchQueryBuilder.toString(), ProjectionsContants.PRODUCT_STATISTICAL_PROJECTIONS_MAPPING_NAME);
		Query countQuery = entityManager.createNativeQuery(countQueryBuilder.toString());
		
		if(pageable.isPaged()) {
			fetchQuery
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize());
		}
		final List<ProductStatisticalProjections> result = fetchQuery.getResultList();
		final int totalElements = (int)countQuery.getSingleResult();
		return new PageImpl<ProductStatisticalProjections>(result, pageable, totalElements);
	}

}
