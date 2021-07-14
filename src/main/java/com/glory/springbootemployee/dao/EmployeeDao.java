package com.glory.springbootemployee.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import com.glory.springbootemployee.entities.Department;
import com.glory.springbootemployee.entities.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDao {

	private static Map<Integer, Employee> employees = null;

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	JdbcTemplate jdbcTemplate;
	static{
		employees = new HashMap<Integer, Employee>();

		employees.put(1001, new Employee(1001, "E-AA", "aa@163.com", 1, new Department(101, "D-AA")));
		employees.put(1002, new Employee(1002, "E-BB", "bb@163.com", 1, new Department(102, "D-BB")));
		employees.put(1003, new Employee(1003, "E-CC", "cc@163.com", 0, new Department(103, "D-CC")));
		employees.put(1004, new Employee(1004, "E-DD", "dd@163.com", 0, new Department(104, "D-DD")));
		employees.put(1005, new Employee(1005, "E-EE", "ee@163.com", 1, new Department(105, "D-EE")));
	}

	private static Integer initId = 1006;

	public void save(Employee employee){
//		if(employee.getId() == null){
//			employee.setId(initId++);
//		}
//
//		employee.setDepartment(departmentDao.getDepartment(employee.getDepartment().getId()));
//		employees.put(employee.getId(), employee);
		//1、定义插入sql,插入变量使用点位符？或者直接使用变量拼接
		String insertSql = "INSERT INTO `employee` (" +
				"  `lastName`," +
				"  `email`," +
				"  `gender`," +
				"  `d_id`," +
				"  `birth`" +
				") " +
				"VALUES" +
				"  (" +
				"    ?," +
				"    ?," +
				"    ?," +
				"    ?," +
				"    ?" +
				"  ) ;";
		//2、通过jdbcTemplate执行sql语句，并填补相应点位符的值
		int insertNum = jdbcTemplate.update(insertSql,
				employee.getLastName(),
				employee.getEmail(),
				employee.getGender(),
				employee.getDepartment().getId(),
				employee.getBirth());
		//3、通过logger打印执行结果
		logger.info("insertNum="+insertNum);
	}

	//查询所有员工
	public Collection<Employee> getAll(){
		List<Employee> resultList = new ArrayList<>();
		//1、编写查询的sql
		String queryEmpListSql="select a.id,lastName,email,gender,d_id,birth,b.departmentName from employee as a,department as b where a.d_id=b.id order by a.id";
		//2、执行查询sql
		List<Map<String, Object>> maps = jdbcTemplate.queryForList(queryEmpListSql);
		//logger.info(maps.toString());
		//3、编辑sql的结果至resultList
		for (Map<String,Object> emp : maps){
			Employee employee= new  Employee(
					(Integer)emp.get("id"),
					(String)emp.get("lastName"),
					(String)emp.get("email"),
					(Integer) emp.get("gender"),
					new Department((Integer) emp.get("d_id"), (String) emp.get("departmentName")));

			employee.setBirth(this.localDateTime2Date((LocalDateTime) emp.get("birth")));
			//employee.setBirth(this.localDateTime2Date(LocalDateTime.from((TemporalAccessor) emp.get("birth"))));
			resultList.add(employee);
		}
		logger.info(String.valueOf(resultList));
		return resultList;
	}

	public Employee get(Integer id){
		/*//拼接的形式
		String getEmployeeSql = "select * from employee t where t.id = "+id;
		Employee employee = new Employee();
		List<Map<String,Object>> empList = jdbcTemplate.queryForList(getEmployeeSql);

		for (Map<String,Object>emp:empList){
			employee = new Employee(
					(Integer) emp.get("id"),
					(String) emp.get("lastName"),
					(String) emp.get("email"),
					(Integer) emp.get("gender"),
					new Department((Integer) emp.get("d_id"),(String) emp.get("departmentName"))
					);
			employee.setBirth(this.localDateTime2Date((LocalDateTime) emp.get("birth")));
		}*/
		//占位符形式
		String getEmployeeSql = "select * from employee t where t.id = ?";
		Employee employee = jdbcTemplate.queryForObject(getEmployeeSql, new RowMapper<Employee>() {
			@Override
			public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
				Employee employee = new Employee(
						resultSet.getInt("id"),
						resultSet.getString("lastName"),
						resultSet.getString("email"),
						resultSet.getInt("gender"),
						new Department(resultSet.getInt("d_id"),null)
				);
				employee.setBirth(resultSet.getDate("birth"));
				return employee;
			}
		},id);
		return employee;
	}

	public void delete(Integer id){
//		employees.remove(id);
		String delEmployeeSql = "delete from employee t where t.id=?";
		int delNum = jdbcTemplate.update(delEmployeeSql,id);
		logger.info("delNum="+delNum);
	}

	public void update(Employee employee){
		String updateEmployeeSql = "update " +
				"  `company`.`employee` " +
				"set" +
				"  `lastName` = ?," +
				"  `email` = ?," +
				"  `gender` = ?," +
				"  `d_id` = ?," +
				"  `birth` = ? " +
				"where `id` = ? ; ";
		Integer updateNum = jdbcTemplate.update(updateEmployeeSql,employee.getLastName(),employee.getEmail(),employee.getGender(),employee.getDepartment().getId(),employee.getBirth(),employee.getId());
		logger.info("updateNum="+updateNum);
	}
	/**
	 * localDateTime转java.util.Date
	 * @param localDateTime
	 * @return
	 */
	public Date localDateTime2Date(LocalDateTime localDateTime) {
		if(null == localDateTime) {
			return null;
		}
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
		return Date.from(zonedDateTime.toInstant());
	}
}
