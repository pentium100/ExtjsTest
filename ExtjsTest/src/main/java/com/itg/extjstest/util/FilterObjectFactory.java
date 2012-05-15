package com.itg.extjstest.util;

import java.lang.reflect.Type;
import java.util.Map;

import com.itg.extjstest.domain.ContractType;

import flexjson.JSONSerializer;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class FilterObjectFactory extends AbstractTransformer implements ObjectFactory {

	@Override
	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {
		// TODO Auto-generated method stub
		if(value instanceof Integer){
			
			return ((Integer)value).toString();

		}else{
			return value.toString();
		}
		
		

		//throw context.cannotConvertValueToTargetType(value, String.class);
		
		
	}
	
	
	@Override
	public void transform(Object object) {
		// TODO Auto-generated method stub
		

	}


}
