package com.itg.extjstest.web;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ResultSetDynaClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;


@Controller
@RequestMapping("/reports")
public class ReportController {
	
	
	@Autowired
	@Qualifier("jdbcTemplate2")
	protected NamedParameterJdbcTemplate jdbcTemplate;

	
	@RequestMapping(value="/noDelivery", headers = "Accept=application/json")
	@ResponseBody   
	public ResponseEntity<String> reportNoDelivery(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "sort", required = false) String sort) {

		
		
		
		StringBuffer query = new StringBuffer();
		query.append("select c.contract_no as contractNo, c.supplier, i.model as model_contract, i.quantity as quantity_in_contract , i.unit_price ,");
		
		query.append("		quantity_no_delivery=(i.quantity-isNull((select SUM(net_weight) from material_doc md left join material_doc_items mds on md.doc_no = mds.material_doc");
		query.append("		                                            left join material_doc_item mi on mi.line_id = mds.items ");
		query.append("                       where md.doc_type = 1 and md.contract = c.id and mi.model_contract = i.model),0))");
		query.append("      from contract c left join contract_items cis on c.id = cis.contract ");
		query.append("                      left join contract_item i on cis.items = i.id");
		query.append("   where (i.quantity-isNull((select SUM(net_weight) from material_doc md left join material_doc_items mds on md.doc_no = mds.material_doc");
		query.append("                                            left join material_doc_item mi on mi.line_id = mds.items ");
		query.append("                       where md.doc_type = 1 and md.contract = c.id and mi.model_contract = i.model),0))>0");
		
		 
		SqlParameterSource param = new MapSqlParameterSource(); 
		List<Map<String,Object>> result= jdbcTemplate.queryForList(query.toString(), param);
		
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", Contract.countContracts());
		map.put("success", true);
		map.put("noDeliverys", result);
		
		String resultJson = new JSONSerializer()
		.exclude("*.class")
		.include("noDeliverys")
		.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),Date.class)
		.serialize(map);


		

		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
	}
	


}
