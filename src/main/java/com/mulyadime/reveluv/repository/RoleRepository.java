package com.mulyadime.reveluv.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mulyadime.reveluv.model.Role;
import com.mulyadime.reveluv.model.User;
import com.mulyadime.reveluv.service.ServiceLocator;
import com.mulyadime.reveluv.utilities.DBConstant;

@Repository
public class RoleRepository extends AbstractRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public static class Field {
		public static String[] PK_APP_ROLE = {
				"PK_APP_ROLE"
				, DBConstant.CriteriaType.LONG
				, DBConstant.OperationType.LONG
		};
		public static String[] ROLE_NAME = {
				"ROLE_NAME"
				, DBConstant.CriteriaType.STRING
				, DBConstant.OperationType.DBL_PERSEN
		};
		
	}
	
	public Hashtable<String, Object> criteriaField = new Hashtable<>();
	public void addCriteriaField() {
		criteriaField.put(Field.PK_APP_ROLE[0], Field.PK_APP_ROLE);
		criteriaField.put(Field.ROLE_NAME[0], Field.ROLE_NAME);
		
	}
	
	public class RoleMapper implements RowMapper<Object> {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role data = new Role();
			data.setId(rs.getLong(Field.PK_APP_ROLE[0]));
			data.setRole(rs.getString(Field.ROLE_NAME[0]));
			
			return data;
		}
		
	}
	
	protected String queryInsert = new StringBuffer()
			.append("INSERT INTO app_user_role (")
			.append("fk_app_user")
			.append(", fk_app_role")
			.append(") VALUES (")
			.append("?")
			.append(", ?")
			.append(")")
			.toString();
	
	protected String queryFindAll = "SELECT * FROM ("
			+ " SELECT ar.*"
			+ " FROM app_role ar"
			+ " ) ar WHERE 1=1";
	
	protected HashMap<String, String> params = new HashMap<>();
	
	protected User user;
	protected Role role;
	
	public RoleRepository() {
		super();
		addCriteriaField();
	}
	
	public int saveUserRole(Object users, Object roles) {
		user = (User) users;
		role = (Role) roles;
		
		Object[] params = new Object[] {
				user.getId()
				, role.getId()
		};
		
		int[] types = new int[] {
				DBConstant.SQLType.LONG
				, DBConstant.SQLType.LONG
		};
		
		return execute(queryInsert, params, types);
	}
	
	public Role findByRole(String param) {
		ServiceLocator.resetParameterValue(params);
		params = new HashMap<>();
		params.put(Field.ROLE_NAME[0], param);
		String sql = construct(queryFindAll, criteriaField, params, "");
		return (Role) jdbcTemplate.queryForObject(sql, new RoleMapper());
	}

}
