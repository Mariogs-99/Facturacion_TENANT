package sv.gov.cnr.cnrpos.utils;

import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import jakarta.persistence.criteria.*;


import java.util.HashMap;
import java.util.Map;

@Component
public class RequestParamParser {

    public static Sort parseSortBy(String sortBy) {
        String[] sortParams = sortBy.split(":");
        String sortField = sortParams.length > 0 ? sortParams[0] : "id";
        String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";

        return Sort.by(Sort.Direction.fromString(sortDirection), sortField);
    }

    public static Map<String, String> parseFilterBy(String filterBy) {
        Map<String, String> filterMap = new HashMap<>();
        String[] filters = filterBy.split(";");

        for (String filter : filters) {
            String[] filterParams = filter.split(":");
            if (filterParams.length == 2) {
                filterMap.put(filterParams[0], filterParams[1]);
            }
        }

        return filterMap;
    }

    public static <T> Specification<T> withFilters(Map<String, String> filters) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(field), "%" + value + "%"));
            }

            return predicate;
        };
    }
}
