package com.mulyadime.reveluv.repository;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mulyadime.reveluv.model.User;
import com.mulyadime.reveluv.utilities.AppUtil;
import com.mulyadime.reveluv.utilities.DBConstant;

@Slf4j
@Repository
public class UserRepository extends AbstractRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public static class Field {
		public static String[] PK_APP_USER = {
				"PK_APP_USER"
				, DBConstant.CriteriaType.LONG
				, DBConstant.OperationType.LONG
		};
		public static String[] USERID = {
				"USERID"
				, DBConstant.CriteriaType.STRING
				, DBConstant.OperationType.DBL_PERSEN
		};
		public static String[] NAME = {
				"NAME"
				, DBConstant.CriteriaType.STRING
				, DBConstant.OperationType.DBL_PERSEN
		};
		public static String[] PASSWORD = {
				"PASSWORD"
				, DBConstant.CriteriaType.STRING
				, DBConstant.OperationType.NO_PERSEN
		};
		public static String[] IS_ACTIVE = {
				"IS_ACTIVE"
				, DBConstant.CriteriaType.INTEGER
				, DBConstant.OperationType.LONG
		};
		
	}
	
	public Hashtable<String, Object> criteriaField = new Hashtable<>();
	public void addCriteriaField() {
		criteriaField.put(Field.PK_APP_USER[0], Field.PK_APP_USER);
		criteriaField.put(Field.USERID[0], Field.USERID);
		criteriaField.put(Field.NAME[0], Field.NAME);
		criteriaField.put(Field.PASSWORD[0], Field.PASSWORD);
		criteriaField.put(Field.IS_ACTIVE[0], Field.IS_ACTIVE);
		
	}
	
	public class UserMapper implements RowMapper<Object> {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getLong(Field.PK_APP_USER[0]));
			user.setUserid(rs.getString(Field.USERID[0]));
			user.setName(rs.getString(Field.NAME[0]));
			user.setPassword(rs.getString(Field.PASSWORD[0]));
			user.setActive(rs.getBoolean(Field.IS_ACTIVE[0]));
			
			return user;
		}
		
	}
	
	protected String queryInsert = new StringBuffer()
			.append("INSERT INTO app_user (")
			.append("pk_app_user")
			.append(", userid")
			.append(", name")
			.append(", password")
			.append(", is_active")
			.append(") VALUES (")
			.append("?")
			.append(", ?")
			.append(", ?")
			.append(", ?")
			.append(", ?")
			.append(")")
			.toString();
	
	protected String queryUpdate = new StringBuffer()
			.append("UPDATE app_user SET ")
			.append(" userid ").append("=").append(" ? ")
			.append(" , name ").append("=").append(" ? ")
			.append(" , password ").append("=").append(" ? ")
			.append(" , is_active ").append("=").append(" ? ")
			.append(" WHERE 1=1 ")
			.append(" AND pk_app_user ").append("=").append(" ? ")
			.toString();
	
	protected String queryDelete = new StringBuffer()
			.append("DELETE FROM app_user ")
			.append(" WHERE 1=1 ")
			.append(" AND pk_app_user ").append("=").append(" ? ")
			.toString();
	
	protected String queryFindAll = "SELECT * FROM ("
			+ " SELECT au.*"
			+ " FROM app_user au"
			+ " ) au WHERE 1=1";
	
	protected User user;
	
	public UserRepository() {
		super();
		addCriteriaField();
	}
	
	public int insert(Object data) {
		user = (User) data;
		user.setId(getNextSequence("app_user"));
		log.debug("{}", user.toString());
		
		Object[] params = new Object[] {
				user.getId()
				, user.getUserid()
				, user.getName()
				, user.getPassword()
				, user.getActive()
		};
		
		int[] types = new int[] {
				DBConstant.SQLType.LONG
				, DBConstant.SQLType.STRING
				, DBConstant.SQLType.STRING
				, DBConstant.SQLType.STRING
				, DBConstant.SQLType.INTEGER
		};
		
		return execute(queryInsert, params, types);
	}
	
	public int update(Object data) {
		user = (User) data;
		Object[] params = new Object[] {
				user.getUserid()
				, user.getName()
				, user.getPassword()
				, user.getActive()
				, user.getId()
		};
		
		int[] types = new int[] {
				DBConstant.SQLType.STRING
				, DBConstant.SQLType.STRING
				, DBConstant.SQLType.STRING
				, DBConstant.SQLType.STRING
				, DBConstant.SQLType.LONG
		};
		
		return execute(queryUpdate, params, types);
	}
	
	public int delete(Object data) {
		Object[] params = new Object[] {
				user.getId()
		};
		
		int[] types = new int[] {
				DBConstant.SQLType.LONG
		};
		
		return execute(queryDelete, params, types);
	}

	public List<Object> findAllByWhereClause(HashMap<String, String> params) {
		String sql = construct(queryFindAll, criteriaField, params, "");
		
		return jdbcTemplate.query(sql, new UserMapper());
	}
	
	public User findByWhereClause(HashMap<String, String> params) {
		String sql = construct(queryFindAll, criteriaField, params, "");
		return (User) jdbcTemplate.queryForObject(sql, new UserMapper());
	}
	
	public int getCount(HashMap<String, String> params) {
		String sql = construct(queryFindAll, criteriaField, params, "");
		sql = "SELECT COUNT(1) FROM (" + sql + ")";
		log.debug("{}", sql);
		
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
	
}