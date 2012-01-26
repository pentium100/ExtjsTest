package com.itg.extjstest.util;

import java.lang.reflect.Type;

import com.itg.extjstest.domain.ContractType;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class ContractTypeObjectFactory extends AbstractTransformer implements ObjectFactory {

	@Override
	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {
		// TODO Auto-generated method stub
		if(value instanceof String){
			if(value.equals("采购合同")){
				return ContractType.PURCHASE;
			}
			if(value.equals("销售合同")){
				return ContractType.SALE;
			}

		}
		
		throw context.cannotConvertValueToTargetType(value, ContractType.class);
		
		
	}
	
	
	@Override
	public void transform(Object object) {
		// TODO Auto-generated method stub
		
		if(object instanceof ContractType){
			ContractType c = (ContractType)object;
			if(c.equals(ContractType.PURCHASE)){
				getContext().writeQuoted("采购合同");	
			}
			if(c.equals(ContractType.SALE)){
				getContext().writeQuoted("销售合同");	
			}

		}

	}


}
