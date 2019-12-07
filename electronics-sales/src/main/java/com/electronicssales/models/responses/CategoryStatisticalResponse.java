package com.electronicssales.models.responses;

import java.util.List;

import com.electronicssales.models.CategoryStatisticalProjections;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor @AllArgsConstructor
public class CategoryStatisticalResponse {

	List<CategoryStatisticalProjections> categoryStatisticals;
	
	public int getTotalProductSold() {
		return this.categoryStatisticals
				.stream()
				.map(CategoryStatisticalProjections::getTotalProductSold)
				.reduce(0, (curr, next) -> curr + next);
	}
	
}
