package com.itg.extjstest.util;

import java.lang.reflect.Type;

import com.itg.extjstest.domain.ContractType;
import com.itg.extjstest.domain.Employee;

import com.itg.extjstest.repository.EmployeeRepository;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmployeeObjectFactory extends AbstractTransformer implements
		ObjectFactory {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {
		// TODO Auto-generated method stub
		if (value instanceof Integer) {
			Integer tmp = (Integer) value;
			Employee emp = employeeRepository.findEmployee(tmp.longValue());

			return emp;
		}

		throw context.cannotConvertValueToTargetType(value, Employee.class);

	}

	@Override
	public void transform(Object object) {
		// TODO Auto-generated method stub

		if (object instanceof ContractType) {
			ContractType c = (ContractType) object;
			if (c.equals(ContractType.PURCHASE)) {
				getContext().writeQuoted("采购合同");
			}
			if (c.equals(ContractType.SALE)) {
				getContext().writeQuoted("销售合同");
			}

		}

	}

}
