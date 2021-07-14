package com.glory.springbootemployee.controller;

import com.glory.springbootemployee.dao.DepartmentDao;
import com.glory.springbootemployee.dao.EmployeeDao;
import com.glory.springbootemployee.entities.Department;
import com.glory.springbootemployee.entities.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author: kaka
 * @date: 2021/6/27
 * @description:
 */
@Controller
public class EmployeeController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
     EmployeeDao employeeDao;
    @Autowired
     DepartmentDao departmentDao;

//    @ResponseBody
    @GetMapping("/emps")
    public String list(Model model) {
        //1.查询所有员工数据
        Collection<Employee> employees= employeeDao.getAll();
        //2.将查义结果放入请求域
        model.addAttribute("emps",employees);
        return "emps/list";
    }

    /**
     * 跳转添加员工页面
     * @param model
     * @return
     */
    @GetMapping("/emp")
    public String add(Model model){
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts",departments);
        return "emps/add";//代表直接去resources目录下相关文件
    }

    /**
     * 添加员工数据
     * @param employee
     * @return
     */
    @PostMapping("/emp")
    public String addEmployee(Employee employee){
        logger.info("employee="+employee);
        //保存员工信息
        employeeDao.save(employee);
        return "redirect:/emps";//代表转发到对应的controller
    }

    /**
     * 跳转修改员工页面
     * @param model
     * @return
     */
    @GetMapping("/emp/{id}")
    public String toEditPage(@PathVariable("id") Integer id, Model model){
        logger.info("id="+id);
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts",departments);
        //1根据id查询员工信息
        Employee employee= employeeDao.get(id);
        //2将员工信息添加到输入域
        model.addAttribute("emp",employee);
       return "emps/add";
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping("/emp")
    public String editEmployee(Employee employee){
        logger.info("edit employee="+employee.toString());
        //保存员工信息
        employeeDao.update(employee);
        return "redirect:/emps";//代表转发到对应的controller
    }

    /**
     * 删除员工
     * @param id
     * @return
     */
    @DeleteMapping("/emp/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id){
        logger.info("del id="+id);
        employeeDao.delete(id);
        return "redirect:/emps";
    }

}
