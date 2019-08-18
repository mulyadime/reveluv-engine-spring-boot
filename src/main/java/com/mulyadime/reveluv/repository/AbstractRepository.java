package com.mulyadime.reveluv.repository;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mulyadime.reveluv.utilities.DBConstant;
import com.mulyadime.reveluv.utilities.AppUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public abstract class AbstractRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public Long getNextSequence(String sequenceName) {
		String sql = String.format("SELECT seq_%s.NEXTVAL FROM dual", sequenceName);
		Long result = jdbcTemplate.queryForObject(sql, Long.class);
		log.debug("Result: {}; {}", result, sql);
		
		return result;
	}
	
	public int execute(String sql, Object[] criteriaValue, int[] types) {
		return jdbcTemplate.update(sql, criteriaValue, types);
	}
	
	protected List<Map<String, Object>> findByCriteria(
			String sql
			, String orderBy
			, Hashtable<String, Object> fields
			, HashMap<String, String> params
			, String alias
	) {
		sql = construct(sql, fields, params, alias);
		String result_query = sql + orderBy;
		log.debug("{}", result_query);
		
		return jdbcTemplate.queryForList(result_query);
	}

	protected String construct(String sql, Hashtable<String, Object> fields, HashMap<String, String> params, String alias) {
		if (params != null && params.size() > 0) {
			String param_field[] = new String[params.size()];
			String param_value[] = new String[params.size()];
			for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
				Object data = it.next();
				int row = 0;
				if (data != null) {
					String field_name = (String) data;
					param_field = (String[]) fields.get(field_name);
					param_value[row] = (String) params.get(field_name);
					
					String where_clause = "";
					if (alias.equalsIgnoreCase(where_clause))
						where_clause = AppUtil.formatToString(" UPPER(%s)", field_name);
					else
						where_clause = AppUtil.formatToString(" UPPER(%s.%s)", new Object[] { alias, field_name }) ;
					
					log.debug("{}", where_clause);
					sql = sql + getOperator(" AND " + where_clause, param_field[2], param_value[row]);
					
					row++;
				}
				
			}
			
		}
		log.debug("{}", sql);
		return sql;
	}

	private String getOperator(String format, String type, String value) {
		String result = "";
		
		if (type.equalsIgnoreCase(DBConstant.OperationType.DBL_PERSEN)) {
			String html_stripped = value.replaceAll("\\<.*?\\>", "");
			html_stripped = html_stripped.replaceAll("\\'*\\'", "''");
			result = " LIKE '%" + html_stripped.toUpperCase() + "%'";
		} else
		if (type.equalsIgnoreCase(DBConstant.OperationType.LONG)
//					|| type.equalsIgnoreCase(DBConstant.OperationType.INTEGER)
			) {
			result = AppUtil.formatToString(" = %s", new Object[] { value.toUpperCase() });
		}
		
		return format + result;
			
	}

}
