package com.itg.extjstest.web;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.itg.extjstest.domain.MaterialDocItem;
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

	@RequestMapping(value = "/openOrders")
	public String OpenOrders(
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

		sortString.append(" model asc ");

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
		StringBuffer cte = new StringBuffer();

		cte.append("with OpenOrder as (");
		cte.append("select (ROW_NUMBER() over (order by "
				+ sortString.toString()
				+ " )) as rowNum, model, quantity_purchases = sum(case when c.contract_type=0 then quantity else 0 end),");
		cte.append("                   quantity_sales = sum(case when c.contract_type=1 then quantity else 0 end),");
		cte.append("                   quantity_open = sum(case when c.contract_type=1 then -quantity else quantity end)");
        
		cte.append("  from contract c inner join contract_items cis on c.id = cis.contract " );
		cte.append("                  inner join contract_item ci on cis.items = ci.id " );
		cte.append(whereString);
		cte.append(" group by model ");
		cte.append(" )");
		
		
		query.append(" select * from OpenOrder where rowNum>:start and rowNum<=:start+:limit");
		

		// SqlParameterSource param = new MapSqlParameterSource();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start", start);
		param.put("limit", limit);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString()+query.toString(), param);
		

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "openOrders");
			map.put("openOrders", result);
			List<ReportHeader> headers = new ArrayList<ReportHeader>();
			ReportHeader header;
			
			header = new ReportHeader();
			header.setHeader("型号");
			header.setField("model");
			header.setPosition(0);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("采购数量");
			header.setField("quantity_purchases");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(1);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("销售数量");
			header.setField("quantity_sales");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(2);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("敞口数量");
			header.setField("quantity_open");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(3);
			headers.add(header);
			
			
			map.put("headers", headers);
			map.put("title", "敞口业务报表");
			
			return "ExportToExcel";
					

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			
			Long recordCount = jdbcTemplate.queryForLong(
					cte.toString()+" select count(*) from OpenOrder", param);
			
			map2.put("total", recordCount);
			map2.put("success", true);
			map2.put("openOrders", result);
			//map2.put("dataRoot", "noDeliverys");

			String resultJson = new JSONSerializer()
					.exclude("*.class")
					.include("openOrders")
					.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
							Date.class).serialize(map2);
			map.put("result", resultJson);

			return "resultOnly";
		}

	}
	
	@RequestMapping(value = "/noDeliverys")
	public String reportNoDeliverys(
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
		StringBuffer cte = new StringBuffer();

		cte.append("with NoDelivery as (");
		cte.append("select (ROW_NUMBER() over (order by "
				+ sortString.toString()
				+ " )) as rowNum, c.contract_no, c.supplier, i.model , i.quantity , i.unit_price ,");

		cte.append("		quantity_no_delivery=(i.quantity-isNull((select SUM(net_weight) from material_doc md left join material_doc_items mds on md.doc_no = mds.material_doc");
		cte.append("		                                            left join material_doc_item mi on mi.line_id = mds.items ");
		cte.append("                       where md.doc_type = 1 and md.contract = c.id and mi.model_contract = i.model),0))");
		cte.append("      from contract c left join contract_items cis on c.id = cis.contract and c.contract_type = 0 ");
		cte.append("                      left join contract_item i on cis.items = i.id");
		cte.append("   where (i.quantity-isNull((select SUM(net_weight) from material_doc md left join material_doc_items mds on md.doc_no = mds.material_doc");
		cte.append("                                            left join material_doc_item mi on mi.line_id = mds.items ");
		cte.append("                       where md.doc_type = 1 and md.contract = c.id and mi.model_contract = i.model),0))>0 ");
		cte.append(whereString);
		cte.append(" )");
		
		query.append(" select *, quantity_in_receipt=quantity-quantity_no_delivery from NoDelivery where rowNum>:start and rowNum<=:start+:limit");
		

		// SqlParameterSource param = new MapSqlParameterSource();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start", start);
		param.put("limit", limit);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString()+query.toString(), param);

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
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(3);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("到货数量");
			header.setField("quantity_in_receipt");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(4);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("未到货数量");
			header.setField("quantity_no_delivery");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(5);
			headers.add(header);
			
			map.put("headers", headers);
			map.put("title", "已签约未到货");
			
			return "ExportToExcel";
					

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			
			Long recordCount = jdbcTemplate.queryForLong(
					cte.toString()+"select count(*) from NoDelivery", param);

			map2.put("total", recordCount);
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
	
	

	
	@RequestMapping(value = "/contractHistorys")
	public String ContractHistorys(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "excel", required = false) String excel,
			ModelMap map) throws ParseException {

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

		sortString.append(" contract_type asc, contract_no asc ");

		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start", start);
		param.put("limit", limit);
		
		
		
		StringBuffer whereString = new StringBuffer();
		for (FilterItem f : filters) {
			
	
			if(!whereString.toString().equals("")){
				whereString.append(" and " );
			}

			whereString.append(f.getSqlWhere());
		}

		StringBuffer query = new StringBuffer();
		StringBuffer cte = new StringBuffer();


		cte.append("with ContractHistory as (");
		cte.append("select (ROW_NUMBER() over (order by "
				+ sortString.toString()
				+ " )) as rowNum, ");
		

		cte.append(" case when contract_type = 0 then '采购合同' else '销售合同' end as contract_type, contract_no,supplier, pay_term, contract.remark, model, quantity,unit_price,contract_item.remark as item_remark  "); 
		cte.append(" from contract");  
		cte.append(" inner join contract_items on contract.id = contract_items.contract    ");
		cte.append(" inner join contract_item on contract_item.id = contract_items.items   ");
		
		
		
		if (!whereString.toString().equals("")){
			cte.append(" where " + whereString);
		}
		cte.append(" )");
		
		query.append(" select * from ContractHistory where rowNum>:start and rowNum<=:start+:limit");
		




		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString()+query.toString(), param);

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "contractHistorys");
			map.put("contractHistorys", result);
			List<ReportHeader> headers = new ArrayList<ReportHeader>();
			ReportHeader header;
			
			header = new ReportHeader();
			header.setHeader("合同类型");
			header.setField("contract_type");
			header.setPosition(0);
			headers.add(header);

			header.setHeader("合同号");
			header.setField("contract_no");
			header.setPosition(1);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("供应商");
			header.setField("supplier");
			header.setPosition(2);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("付款方式");
			header.setField("pay_term");
			header.setPosition(3);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("备注");
			header.setField("remark");
			header.setPosition(3);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("规格");
			header.setField("model");
			header.setPosition(2);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("签约数量");
			header.setField("quantity");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(3);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("单价");
			header.setField("unit_price");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(3);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("备注");
			header.setField("item_remark");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setPosition(3);
			headers.add(header);
			
			
			map.put("headers", headers);
			map.put("title", "合同查询");
			
			return "ExportToExcel";
					

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			
			Long recordCount = jdbcTemplate.queryForLong(
					cte.toString()+"select count(*) from ContractHistory", param);

			map2.put("total", recordCount);
			map2.put("success", true);
			map2.put("contractHistorys", result);
			//map2.put("dataRoot", "noDeliverys");

			String resultJson = new JSONSerializer()
					.exclude("*.class")
					.include("contractHistorys")
					.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
							Date.class).serialize(map2);
			map.put("result", resultJson);

			return "resultOnly";
		}

	}
	

	
	@RequestMapping(value = "/stockQuerys")
	public String stockQuerys(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "excel", required = false) String excel,
			ModelMap map) throws ParseException {

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

		sortString.append(" model asc ");

		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}

		
		
		StringBuffer whereString = new StringBuffer();
		for (FilterItem f : filters) {
			
			if(!whereString.toString().equals("")){
				whereString.append(" and " );
			}

			whereString.append(f.getSqlWhere());
		}

		
		Map<String, Object> param = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		param.put("endDate", sdf.parse(endDate));
		param.put("start", start);
		param.put("limit", limit);
		
		
		StringBuffer query = new StringBuffer();
		StringBuffer cte = new StringBuffer();

		
		cte.append("with StockQuery as (");
		cte.append("select (ROW_NUMBER() over (order by "
				+ sortString.toString()
				+ " )) as rowNum, ");
		

		cte.append("     material_doc.batch_no, material_doc.delivery_note, material_doc.doc_date, material_doc.plate_num, material_doc.working_no, "); 
		cte.append("     stock.warehouse, stock.net_weight, ");
		cte.append("     material_doc_item.model_contract, material_doc_item.model_tested, ");
		cte.append("     contract_item.unit_price, contract.contract_no,contract.supplier ");
		cte.append(" from material_doc  ");
		cte.append("      inner join material_doc_item on material_doc_item.material_doc = material_doc.doc_no ");
		cte.append("      inner join contract on contract.id =  material_doc.contract ");
		cte.append("      left join contract_item on material_doc.contract = contract_item.contract "); 
		cte.append("                             and material_doc_item.model_contract = contract_item.model, "); 
		cte.append(" ( ");
		cte.append("  select line_id_in, warehouse, SUM(net_weight*direction) as net_weight ");
		cte.append("     from material_doc_item  ");
		cte.append("     inner join material_doc_items on material_doc_items.items = material_doc_item.line_id ");
		cte.append("     inner join material_doc on material_doc.doc_no = material_doc_items.material_doc and material_doc.doc_date <= :endDate ");
		cte.append("     group by line_id_in,warehouse ");
		cte.append("     having SUM(net_weight*direction)>0) stock ");
		cte.append("  where material_doc_item.line_id = stock.line_id_in ");
   
	      
		
		
		if (!whereString.toString().equals("")){
			cte.append(" and " + whereString);
		}
		cte.append(" )");
		
		query.append(" select * from StockQuery where rowNum>:start and rowNum<=:start+:limit");
		
		
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString()+query.toString(), param);

		

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "stockQuerys");
			map.put("stockQuerys", result);
			List<ReportHeader> headers = new ArrayList<ReportHeader>();
			ReportHeader header;
			
			header = new ReportHeader();
			header.setHeader("合同号");
			header.setField("contract_no");
			headers.add(header);
			

			header = new ReportHeader();
			header.setHeader("供应商");
			header.setField("supplier");
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("进仓单号");
			header.setField("deliveryNote");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("进仓日期");
			header.setField("doc_date");
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("车号/卡号");
			header.setField("plate_num");
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("批次号");
			header.setField("batch_no");
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("规格(检验后)");
			header.setField("model_tested");
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("规格(合同)");
			header.setField("model_contract");
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("净重");
			header.setField("net_weight");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("单价");
			header.setField("unit_price");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);
			
			header = new ReportHeader();
			header.setHeader("仓库");
			header.setField("warehouse");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);
		
			
			map.put("headers", headers);
			map.put("title", "敞口业务报表");
			
			return "ExportToExcel";
					

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			
			Long recordCount = jdbcTemplate.queryForLong(
					cte.toString()+"select count(*) from StockQuery", param);

			
			map2.put("total", recordCount);
			map2.put("success", true);
			map2.put("stockQuerys", result);
			//map2.put("dataRoot", "noDeliverys");

			String resultJson = new JSONSerializer()
					.exclude("*.class")
					.include("stockQuerys")
					.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
							Date.class).serialize(map2);
			map.put("result", resultJson);

			return "resultOnly";
		}

	}

}
