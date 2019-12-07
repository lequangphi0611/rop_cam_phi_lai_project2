package com.electronicssales.repositories.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.electronicssales.models.CategoryStatisticalProjections;
import com.electronicssales.models.RevenueOverMonthStatisticalProjections;
import com.electronicssales.models.RevenueProductStatisticalProjections;
import com.electronicssales.models.ProjectionsContants;
import com.electronicssales.repositories.BaseStatisticalRepository;
import com.electronicssales.repositories.StatisticalRepository;

@SuppressWarnings("unchecked")
public class BaseStatisticalRepositoryImpl implements BaseStatisticalRepository {
	
	private static final String CATEGORY_STATISTICAL_QUERY = "SELECT c.category_name AS categoryName, " 
						+ "COUNT(DISTINCT p.id) AS productCount, "
						+ "SUM(td.quantity) AS totalProductSold "
						+ "FROM categories c " 
						+ "LEFT JOIN product_categories pc ON c.id = pc.category_id "
						+ "INNER JOIN products p ON pc.product_id = p.id " 
						+ "LEFT JOIN transaction_detaileds td ON td.product_id = p.id "
						+ "WHERE c.parent_id IS NULL "
						+ "GROUP BY c.category_name "
						+ "ORDER BY totalProductSold desc";
	
	private static final String REVENUE_PRODUCT_STATISTICAL_QUERY = "SELECT TOP (?1) p.product_name AS productName, " +
			"(SELECT TOP 1 i.data " + 
			"   FROM images i " + 
			"   INNER JOIN product_images pis ON pis.image_id = i.id " + 
			"   WHERE pis.product_id = p.id) AS image, " +
			"           COUNT(td.id) AS nusmberOfSales, " + 
			"           SUM(td.quantity) AS quantityProductSold, " + 
			"           SUM((td.price - (CASE td.discount_type " + 
			"                                WHEN 'PERCENT' THEN (td.price * td.discount_value / 100) " + 
			"                                WHEN 'AMOUNT' THEN td.discount_value " + 
			"                                ELSE 0 " + 
			"                            END)) * td.quantity) AS revenue " + 
			"FROM products p " + 
			"INNER JOIN transaction_detaileds td ON p.id = td.product_id " + 
			"GROUP BY p.product_name, p.id " + 
			"ORDER BY revenue DESC";

		private static final String REVENUE_OVER_MONTH_STATISTICAL_QUERY = "SELECT MONTH(t.created_time) as month,"
				+ "SUM((td.price - (CASE td.discount_type "
										+ "WHEN 'PERCENT' THEN (td.price * td.discount_value / 100) "
										+ "WHEN 'AMOUNT' THEN td.discount_value "
										+ "ELSE 0 "
									+ "END)) * td.quantity) AS revenue "
			+ "FROM transactions t " 
			+ "INNER JOIN transaction_detaileds td " 
			+	"ON t.id = td.transaction_id "
			+ "WHERE YEAR(t.created_time) = YEAR(GETDATE()) "
			+ "GROUP BY MONTH(t.created_time)"
			+ "ORDER BY month asc";
	
	private static final int DEFAULT_TOP_QUERY_PARAMETER = 5;

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<CategoryStatisticalProjections> getCategoryStatistical() {
		return entityManager.createNativeQuery(
					CATEGORY_STATISTICAL_QUERY, 
					ProjectionsContants.CATEGORY_STATISTICAL_MAPPING_NAME)
				.getResultList();
	}

	@Override
	public List<RevenueProductStatisticalProjections> getRevenueProductStatistical(Integer top) {
		return entityManager
				.createNativeQuery(
					REVENUE_PRODUCT_STATISTICAL_QUERY,
					ProjectionsContants.REVENUE_PRODUCT_STATISTICAL_MAPPING_NAME
				)
				.setParameter(1, top == null ? DEFAULT_TOP_QUERY_PARAMETER : top)
				.getResultList();
	}

	@Override
	public List<RevenueOverMonthStatisticalProjections> getRevenueMonthStatistical() {
		return entityManager.createNativeQuery(
				REVENUE_OVER_MONTH_STATISTICAL_QUERY, 
				ProjectionsContants.REVENUE_OVER_MONTH_STATISTICAL_PROJECTIONS_MAPPING
			)
			.getResultList();
	}
}
