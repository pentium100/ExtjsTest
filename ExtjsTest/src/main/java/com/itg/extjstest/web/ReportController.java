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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.Message;
import com.itg.extjstest.util.FilterItem;
import com.itg.extjstest.util.ReportHeader;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@Controller
@RequestMapping("/reports")
public class ReportController {

	@Autowired
	@Qualifier("jdbcTemplate2")
	protected NamedParameterJdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/noDelivery")
	public String reportNoDelivery(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "excel", required = false) String excel,
			ModelMap map) {

		List<Map<String, String>> sorts = new ArrayList<Map<String, String>>();
		if (sort != null && (!sort.equals(""))) {
			sorts = new JSONDeserializer<List<Map<String, String>>>()
					.deserialize(sort);
		}

		StringBuffer sortString = new StringBuffer();

		for (Map<String, String> s : sorts) {
			if (!sortString.toString().equals("")) {
				sortString.append(",");
			}

			sortString.append(" " + s.get("property") + " "
					+ s.get("direction"));
		}
		if (!sortString.toString().equals("")) {
			sortString.append(",");
		}

		sortString.append(" c.contract_no asc ");

		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}

		StringBuffer whereString = new StringBuffer();
		for (FilterItem f : filters) {

			whereString.append(" and " + f.getSqlWhere());
		}

		StringBuffer query = new StringBuffer();

		query.append("with NoDelivery as (");
		query.append("select (ROW_NUMBER() over (order by "
				+ sortString.toString()
				+ " )) as rowNum, c.contract_no, c.supplier, i.model , i.quantity , i.unit_price ,");

		query.append("		quantity_no_delivery=(i.quantity-isNull((select SUM(net_weight) from material_doc md left join material_doc_items mds on md.doc_no = mds.material_doc");
		query.append("		                                            left join material_doc_item mi on mi.line_id = mds.items ");
		query.append("                       where md.doc_type = 1 and md.contract = c.id and mi.model_contract = i.model),0))");
		query.append("      from contract c left join contract_items cis on c.id = cis.contract ");
		query.append("                      left join contract_item i on cis.items = i.id");
		query.append("   where (i.quantity-isNull((select SUM(net_weight) from material_doc md left join material_doc_items mds on md.doc_no = mds.material_doc");
		query.append("                                            left join material_doc_item mi on mi.line_id = mds.items ");
		query.append("                       where md.doc_type = 1 and md.contract = c.id and mi.model_contract = i.model),0))>0 ");
		query.append(whereString);
		query.append(" )");
		query.append(" select * from NoDelivery where rowNum>:start and rowNum<=:start+:limit");

		// SqlParameterSource param = new MapSqlParameterSource();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start", start);
		param.put("limit", limit);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				query.toString(), param);

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "noDeliverys");
			map.put("noDeliverys", result);
			List<ReportHeader> headers = new ArrayList<ReportHeader>();
			ReportHeader header;
			
			header = new ReportHeader();
			header.setHeader("合同号");
			header.setField("contract_no");
			header.setPosition(0);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("供应商");
			header.setField("supplier");
			header.setPosition(1);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("规格");
			header.setField("model");
			header.setPosition(2);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("签约数量");
			header.setField("quantity");
			header.setPosition(3);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("未到货数量");
			header.setField("quantity_no_delivery");
			header.setPosition(4);
			headers.add(header);
			
			map.put("headers", headers);
			map.put("title", "已签约未到货");
			
			return "ExportToExcel";
					

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("total", Contract.countContracts());
			map2.put("success", true);
			map2.put("noDeliverys", result);
			//map2.put("dataRoot", "noDeliverys");

			String resultJson = new JSONSerializer()
					.exclude("*.class")
					.include("noDeliverys")
					.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
							Date.class).serialize(map2);
			map.put("result", resultJson);

			return "resultOnly";
		}

	}
}
