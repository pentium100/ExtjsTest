package com.itg.extjstest.util;

import java.lang.reflect.Type;

import com.itg.extjstest.domain.ContractType;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.factories.BeanObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class ContractObjectFactory extends BeanObjectFactory {

	@Override
	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {
		
		if(value==null||value.equals("")){
			return null;
		}else{
			return super.instantiate(context, value, targetType, targetClass);
		}
		
		//throw context.cannotConvertValueToTargetType(value, targetClass);
		
		
	}
	
	



}
