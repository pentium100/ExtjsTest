package com.itg.extjstest.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.itg.extjstest.domain.OpenOrderMemo;
import com.itg.extjstest.util.FilterItem;
import com.itg.extjstest.util.FilterObjectFactory;
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

	@RequestMapping(value = "/openOrders/{model}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateOpenOrderMemo(@RequestBody String json,
			HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		OpenOrderMemo openOrderMemo = OpenOrderMemo
				.fromJsonToOpenOrderMemo(json);
		OpenOrderMemo o1 = null;
		try {
			o1 = OpenOrderMemo.findOpenOrderMemoesByModelEquals(
					openOrderMemo.getModel()).getSingleResult();
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			o1 = new OpenOrderMemo();
			o1.setModel(openOrderMemo.getModel());
			o1.setId(null);
		}
		o1.setMemo(openOrderMemo.getMemo());
		o1.setUpdateTime(new Date());
		o1.setUpdateUser(request.getRemoteUser());

		o1 = o1.merge();
		o1.setUpdate_time(new Date());
		o1.setUpdate_user(request.getRemoteUser());

		List<OpenOrderMemo> openOrderMemos = new ArrayList<OpenOrderMemo>();
		openOrderMemos.add(o1);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		String resultJson = OpenOrderMemo.mapToJson(map, openOrderMemos);

		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/openOrders", method = RequestMethod.GET)
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
				+ sortString.toString() + ")) as rowNum, ");

		cte.append(" case when GROUPING(model)=1 then '总计' else model end as model, ");
		cte.append(" quantity_purchases = sum(case when c.contract_type=0 then quantity else 0 end),");
		cte.append("                   quantity_sales = sum(case when c.contract_type=1 then quantity else 0 end),");
		cte.append("                   quantity_open = sum(case when c.contract_type=1 then -quantity else quantity end)");

		cte.append("  from contract c inner join contract_items cis on c.id = cis.contract ");
		cte.append("                  inner join contract_item ci on cis.items = ci.id ");
		cte.append(whereString);
		cte.append(" group by grouping sets(model,()) ");
		cte.append(" )");

		query.append(" select OpenOrder.*, open_order_memo.memo, open_order_memo.update_user, open_order_memo.update_time from OpenOrder ");
		query.append("          left join open_order_memo on open_order_memo.model = OpenOrder.model ");
		query.append("   where rowNum>:start and rowNum<=:start+:limit");

		// SqlParameterSource param = new MapSqlParameterSource();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start", start);
		param.put("limit", limit);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString() + query.toString(), param);

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
			header.setFormat("#,##0.000");
			
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("销售数量");
			header.setField("quantity_sales");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setFormat("#,##0.000");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("敞口数量");
			header.setField("quantity_open");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setFormat("#,##0.000");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("备注");
			header.setField("memo");
			headers.add(header);

			map.put("headers", headers);
			map.put("title", "敞口业务报表");

			return "ExportToExcel";

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();

			Long recordCount = jdbcTemplate.queryForLong(cte.toString()
					+ " select count(*) from OpenOrder", param);

			map2.put("total", recordCount);
			map2.put("success", true);
			map2.put("openOrders", result);
			// map2.put("dataRoot", "noDeliverys");

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

		sortString.append(" contract_no asc ");

		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					.use("values.value", new FilterObjectFactory() )
					.deserialize(filter);
					
					

		}

		StringBuffer whereString = new StringBuffer();
		StringBuffer whereString2 = new StringBuffer();
		for (FilterItem f : filters) {
			
			if(f.getField().equals("quantity")||
			   f.getField().equals("quantity_no_delivery")||
			   f.getField().equals("quantity_in_receipt")){
				
				whereString2.append(" and " + f.getSqlWhere());
			}else{
				whereString.append(" and " + f.getSqlWhere());
			}
		}

		StringBuffer query = new StringBuffer();
		StringBuffer cte = new StringBuffer();

		cte.append("with NoDelivery as (");
		cte.append("select " );
		//cte.append("ROW_NUMBER() over (order by "
		//		+ sortString.toString()
		//		+ " )) as rowNum, ");
		
		cte.append("      case when contract_type = 0 then '采购合同' else '销售合同' end as contract_type, c.contract_no, c.supplier, i.model , i.quantity , i.unit_price, c.sign_date, ");

		cte.append("      quantity_no_delivery=(i.quantity-isNull((select SUM(net_weight) from material_doc md left join material_doc_items mds on md.doc_no = mds.material_doc");
		cte.append("                                           left join material_doc_item mi on mi.line_id = mds.items ");
		cte.append("                       where (md.doc_type = 1 or md.doc_type=2) and mi.contract = c.id and mi.model_contract = i.model),0))");
		cte.append("      from contract c ");
		cte.append("                   left join contract_item i on i.contract = c.id");
		cte.append("   where (i.quantity-isNull((select SUM(net_weight) from material_doc md ");
		cte.append("                                            left join material_doc_item mi on mi.material_doc = md.doc_no ");
		cte.append("                       where ( md.doc_type = 1 or md.doc_type = 2 ) and mi.contract = c.id and mi.model_contract = i.model),0))>0 ");
		cte.append(whereString);
		cte.append(" )");

		//query.append(" select *, quantity_in_receipt=quantity-quantity_no_delivery from NoDelivery where rowNum>:start and rowNum<=:start+:limit");
		query.append(" select *, quantity_in_receipt=quantity-quantity_no_delivery from NoDelivery where 1 = 1 ");		
		query.append(whereString2);
		query.append(" order by " + sortString.toString() +" ");

		// SqlParameterSource param = new MapSqlParameterSource();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start", start);
		param.put("limit", limit);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString() + query.toString(), param);

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "noDeliverys");
			map.put("noDeliverys", result);
			List<ReportHeader> headers = new ArrayList<ReportHeader>();
			ReportHeader header;

			header = new ReportHeader();

			header.setHeader("合同类型");
			header.setField("contract_type");
			header.setPosition(0);
			headers.add(header);

			header = new ReportHeader();
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
			header.setHeader("规格");
			header.setField("model");
			header.setPosition(3);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("签约数量");
			header.setField("quantity");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setFormat("#,##0.000");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("执行数量");
			header.setField("quantity_in_receipt");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setFormat("#,##0.000");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("未执行数量");
			header.setField("quantity_no_delivery");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setFormat("#,##0.000");
			headers.add(header);

			map.put("headers", headers);
			map.put("title", "合同执行情况表");

			return "ExportToExcel";

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();

			Long recordCount = jdbcTemplate.queryForLong(cte.toString()
					+ "select count(*) from NoDelivery", param);

			map2.put("total", recordCount);
			map2.put("success", true);
			map2.put("noDeliverys", result);
			// map2.put("dataRoot", "noDeliverys");

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

			if (!whereString.toString().equals("")) {
				whereString.append(" and ");
			}

			whereString.append(f.getSqlWhere());
		}

		StringBuffer query = new StringBuffer();
		StringBuffer cte = new StringBuffer();

		cte.append("with ContractHistory as (");
		cte.append("select (ROW_NUMBER() over (order by "
				+ sortString.toString() + " )) as rowNum, ");

		cte.append(" case when contract_type = 0 then '采购合同' else '销售合同' end as contract_type, contract_no,supplier, pay_term, contract.remark, model, quantity,unit_price,contract_item.remark as item_remark, contract.sign_date  ");
		cte.append(" from contract");
		cte.append(" inner join contract_items on contract.id = contract_items.contract    ");
		cte.append(" inner join contract_item on contract_item.id = contract_items.items   ");

		if (!whereString.toString().equals("")) {
			cte.append(" where " + whereString);
		}
		cte.append(" )");

		query.append(" select * from ContractHistory where rowNum>:start and rowNum<=:start+:limit");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString() + query.toString(), param);

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

			header = new ReportHeader();
			header.setHeader("合同号");
			header.setField("contract_no");
			header.setPosition(1);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("供应商/客户");
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
			header.setFormat("#,##0.000");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("单价");
			header.setField("unit_price");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			header.setFormat("#,##0.00");
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

			List<Map<String, Object>> summary = jdbcTemplate
					.queryForList(
							cte.toString()
									+ "select count(*) as reccount, sum(quantity) as quantity from ContractHistory",
							param);

			map2.put("total", summary.get(0).get("reccount"));
			map2.put("success", true);
			map2.put("contractHistorys", result);
			map2.put("remoteSummary", summary.get(0));

			// map2.put("dataRoot", "noDeliverys");

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

			if (!whereString.toString().equals("")) {
				whereString.append(" and ");
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
				+ sortString.toString() + " )) as rowNum, ");

		cte.append("     material_doc.batch_no, material_doc.delivery_note, material_doc.doc_date, material_doc.plate_num, material_doc.working_no, ");
		cte.append("     stock.stock_location, stock.net_weight, material_doc_item.gross_weight, ");
		cte.append("     material_doc_item.model_contract, material_doc_item.model_tested,  material_doc_item.remark, ");
		cte.append("     contract_item.unit_price, contract.contract_no,contract.supplier, ");
		cte.append("     convert(varchar(40),stock.line_id_in)+'--'+stock.stock_location as report_key, ");
		cte.append("     inspection.inspection_date, inspection.authority,inspection.doc_no,inspection.original,inspection_item.remark as inspection_remark, ");
		cte.append("     inspection_item.al, inspection_item.ca, inspection_item.fe, inspection_item.p,inspection_item.si ");
		cte.append(" from material_doc  ");
		cte.append("      inner join material_doc_item on material_doc_item.material_doc = material_doc.doc_no ");
		cte.append("      inner join contract on contract.id =  material_doc_item.contract ");
		cte.append("      left join contract_item on material_doc_item.contract = contract_item.contract ");
		cte.append("                             and material_doc_item.model_contract = contract_item.model ");
		cte.append("      left join inspection_item on inspection_item.material_doc_item = material_doc_item.line_id_test ");
		cte.append("      left join inspection on inspection.id = inspection_item.inspection, ");

		cte.append(" ( ");
		cte.append("  select line_id_in, stock_location.stock_location, SUM(net_weight*direction) as net_weight, SUM(material_doc_item.gross_weight*direction) as gross_weight ");
		cte.append("     from material_doc_item  ");
		cte.append("     inner join material_doc_items on material_doc_items.items = material_doc_item.line_id ");
		cte.append("     inner join stock_location on stock_location.id = material_doc_item.stock_location ");
		cte.append("     inner join material_doc on material_doc.doc_no = material_doc_items.material_doc and material_doc.doc_date <= :endDate ");
		cte.append("     group by line_id_in,stock_location.stock_location ");
		cte.append("     having SUM(net_weight*direction)<>0) stock ");
		cte.append("  where material_doc_item.line_id = stock.line_id_in ");

		if (!whereString.toString().equals("")) {
			cte.append(" and " + whereString);
		}
		cte.append(" )");

		query.append(" select * from StockQuery where rowNum>:start and rowNum<=:start+:limit");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString() + query.toString(), param);

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "stockQuerys");
			map.put("stockQuerys", result);
			List<ReportHeader> headers = new ArrayList<ReportHeader>();
			ReportHeader header;
			// 合同号，供应商，规格（合同），单价，毛重，净重，进仓单号，车号/卡号，批次号，仓库，检验信息
			header = new ReportHeader();
			header.setHeader("合同号");
			header.setField("contract_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("供应商");
			header.setField("supplier");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("规格(合同)");
			header.setField("model_contract");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("规格(检验后)");
			header.setField("model_tested");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("单价");
			header.setField("unit_price");
			header.setFormat("#,##0.00");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("毛重");
			header.setField("gross_weight");
			header.setFormat("#,##0.000");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("净重");
			header.setField("net_weight");
			header.setFormat("#,##0.000");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("进仓单号");
			header.setField("deliveryNote");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("车号/卡号");
			header.setField("plate_num");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("进仓日期");
			header.setField("doc_date");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("批次号");
			header.setField("batch_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("仓库");
			header.setField("stock_location");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("备注");
			header.setField("remark");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("检验日期");
			header.setField("inspection_date");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("检验机构");
			header.setField("authority");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("si");
			header.setField("si");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("fe");
			header.setField("fe");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("al");
			header.setField("al");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("ca");
			header.setField("ca");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("p");
			header.setField("p");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("检验备注");
			header.setField("inspection_remark");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("证书编号");
			header.setField("doc_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("正本");
			header.setField("original");
			headers.add(header);

			map.put("headers", headers);
			map.put("title", "库存报表");

			return "ExportToExcel";

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();

			List<Map<String, Object>> summary = jdbcTemplate
					.queryForList(
							cte.toString()
									+ "select count(*) as reccount, sum(net_weight) as net_weight from StockQuery",
							param);

			map2.put("total", summary.get(0).get("reccount"));
			map2.put("success", true);
			map2.put("stockQuerys", result);
			map2.put("remoteSummary", summary.get(0));

			String resultJson = new JSONSerializer()
					.exclude("*.class")
					.include("stockQuerys")
					.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
							Date.class).serialize(map2);
			map.put("result", resultJson);

			return "resultOnly";
		}

	}

	@RequestMapping(value = "/inspectionDetails")
	public String inspectionDetails(
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

		sortString.append(" inspection_date desc ");

		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}

		StringBuffer whereString = new StringBuffer();
		for (FilterItem f : filters) {

			if (!whereString.toString().equals("")) {
				whereString.append(" and ");
			}

			whereString.append(f.getSqlWhere());
		}

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("start", start);
		param.put("limit", limit);

		StringBuffer query = new StringBuffer();
		StringBuffer cte = new StringBuffer();

		cte.append("with InspectionDetail as (");
		cte.append("select (ROW_NUMBER() over (order by "
				+ sortString.toString() + " )) as rowNum, ");

		cte.append("  inspection_date, authority, inspection.doc_no, original,   ");
		cte.append("   al, ca, fe, inspection_item.net_weight, p, inspection_item.remark, si, ");
		cte.append("   material_doc_item.model_contract, material_doc.doc_date, ");
		cte.append("   contract.contract_no, contract.supplier, material_doc.batch_no, material_doc.plate_num, material_doc.delivery_note ");

		cte.append(" FROM inspection join inspection_item on inspection.id = inspection_item.inspection ");
		cte.append(" 				join material_doc_item on material_doc_item.line_id = inspection_item.material_doc_item ");
		cte.append("				join material_doc on material_doc.doc_no = material_doc_item.material_doc ");
		cte.append("	            join contract on contract.id = material_doc_item.contract ");

		if (!whereString.toString().equals("")) {
			cte.append(" where " + whereString);
		}
		cte.append(" )");

		query.append(" select * from InspectionDetail where rowNum>:start and rowNum<=:start+:limit");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString() + query.toString(), param);

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "inspectionDetails");
			map.put("inspectionDetails", result);
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
			header.setHeader("规格(合同)");
			header.setField("model_contract");
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
			header.setHeader("进仓单号");
			header.setField("delivery_note");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("进仓日期");
			header.setField("doc_date");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("检验日期");
			header.setField("inspection_date");

			headers.add(header);

			header = new ReportHeader();
			header.setHeader("机构");
			header.setField("authority");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("si");
			header.setField("si");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("fe");
			header.setField("fe");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("al");
			header.setField("al");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("ca");
			header.setField("ca");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("p");
			header.setField("p");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("备注");
			header.setField("remark");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("编号");
			header.setField("doc_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("正本");
			header.setField("original");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("数量");
			header.setField("net_weight");
			header.setFormat("#,##0.000");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			map.put("headers", headers);
			map.put("title", "检验报告");

			return "ExportToExcel";

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();

			Long recordCount = jdbcTemplate.queryForLong(cte.toString()
					+ "select count(*) from InspectionDetail", param);

			map2.put("total", recordCount);
			map2.put("success", true);
			map2.put("inspectionDetails", result);
			// map2.put("dataRoot", "noDeliverys");

			String resultJson = new JSONSerializer()
					.exclude("*.class")
					.include("inspectionDetails")
					.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
							Date.class).serialize(map2);
			map.put("result", resultJson);

			return "resultOnly";
		}
	}

	@RequestMapping(value = "/afloatGoodsDetails")
	public String afloatGoodsDetails(
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

		sortString.append(" dispatch_date desc ");

		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}

		StringBuffer whereString = new StringBuffer();
		for (FilterItem f : filters) {

			if (!whereString.toString().equals("")) {
				whereString.append(" and ");
			}

			whereString.append(f.getSqlWhere());
		}

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("start", start);
		param.put("limit", limit);

		StringBuffer query = new StringBuffer();
		StringBuffer cte = new StringBuffer();

		cte.append("with AfloatGoodsDetail as (");
		cte.append("select (ROW_NUMBER() over (order by "
				+ sortString.toString() + " )) as rowNum, ");

		cte.append("    contract.contract_no,  ");
		cte.append("    supplier, plate_num, dispatch, destination, transport_date,  ");
		cte.append("	dispatch_date, afloat_goods.eta, arrival_date, original, afloat_goods.remark, ");
		cte.append("	afloat_goods_item.model, beyond_days = case when arrival_date is null then DATEDIFF(day, afloat_goods.eta, getDate()) else 0 end, ");
		cte.append("	afloat_goods_item.batch_no, afloat_goods_item.quantity ");

		cte.append("   FROM afloat_goods  ");
		cte.append("   left join contract on afloat_goods.contract = contract.id ");
		cte.append("   left join afloat_goods_item on  afloat_goods.id = afloat_goods_item.afloat_goods ");

		if (!whereString.toString().equals("")) {
			cte.append(" where " + whereString);
		}
		cte.append(" )");

		query.append(" select * from AfloatGoodsDetail where rowNum>:start and rowNum<=:start+:limit");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString() + query.toString(), param);

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "afloatGoodsDetails");
			map.put("afloatGoodsDetails", result);
			List<ReportHeader> headers = new ArrayList<ReportHeader>();
			ReportHeader header;
			// 合同号，供应商，规格，数量，车号，发货日期，发货地点，到达地，预计到货日期，超期天数，实际到货日期，批次号，正本，转货时间，备注
			header = new ReportHeader();
			header.setHeader("合同号");
			header.setField("contract_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("供应商");
			header.setField("supplier");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("规格");
			header.setField("model");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("数量");
			header.setField("quantity");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("车号/卡号");
			header.setField("plate_num");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("发货日期");
			header.setField("dispatch_date");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("发货");
			header.setField("dispatch");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("到达");
			header.setField("destination");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("预计到货日期");
			header.setField("eta");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("超期天数");
			header.setField("beyond_days");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("实际到货日期");
			header.setField("arrival_date");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("批次号");
			header.setField("batch_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("正本");
			header.setField("original");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("转货时间");
			header.setField("transport_date");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("备注");
			header.setField("remark");
			headers.add(header);

			map.put("headers", headers);
			map.put("title", "在途信息");

			return "ExportToExcel";

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();

			Long recordCount = jdbcTemplate.queryForLong(cte.toString()
					+ "select count(*) from AfloatGoodsDetail", param);

			map2.put("total", recordCount);
			map2.put("success", true);
			map2.put("afloatGoodsDetails", result);

			// map2.put("dataRoot", "noDeliverys");

			String resultJson = new JSONSerializer()
					.exclude("*.class")
					.include("afloatGoodsDetails")
					.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
							Date.class).serialize(map2);
			map.put("result", resultJson);

			return "resultOnly";
		}
	}

	@RequestMapping(value = "/materialDocItemQuerys")
	public String materialDocItemQuery(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "showIncoming", required = false) Boolean showIncoming,
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

		sortString.append(" doc_date asc ");

		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}

		StringBuffer whereString = new StringBuffer();
		StringBuffer whereOutside = new StringBuffer();
		for (FilterItem f : filters) {

			if (f.getField().equals("doc_type_txt")) {
				whereOutside.append(f.getSqlWhere());

			} else {
				if (!whereString.toString().equals("")) {
					whereString.append(" and ");
				}

				whereString.append(f.getSqlWhere());
			}
		}

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("start", start);
		param.put("limit", limit);

		StringBuffer query = new StringBuffer();
		StringBuffer cte = new StringBuffer();

		cte.append("with MaterialDocItemQuery1 as (");
		cte.append("  select doc_type_txt, material_doc.doc_no, contract.contract_no, contract.supplier, ");
		cte.append("       material_doc.delivery_note,  ");
		cte.append("       case when material_doc_type.id=1  ");
		cte.append("                 then  material_doc.delivery_note  else item_in_doc.delivery_note end ");
		cte.append("       as delivery_note_in, ");

		cte.append("       case when material_doc_type.id=2  ");
		cte.append("                 then  material_doc.delivery_note  end ");
		cte.append("       as delivery_note_out, ");
		cte.append("       contract_in.contract_no as purchase_contract_no, ");
		cte.append("       material_doc.doc_date, item_in_doc.plate_num, ");
		cte.append("       item_in_doc.batch_no, material_doc_item.model_contract, material_doc_item.model_tested, ");
		cte.append("       material_doc_item.net_weight*material_doc_item.direction as net_weight, ");
		cte.append("       material_doc_item.gross_weight, stock_location.stock_location, ");
		cte.append("       contract_item.unit_price, item_in_doc.working_no ");
		cte.append("	   from material_doc_item ");
		cte.append("	      left join material_doc on material_doc.doc_no = material_doc_item.material_doc ");
		cte.append("          left join stock_location on stock_location.id = material_doc_item.stock_location");
		cte.append("	      left join contract on contract.id = material_doc_item.contract ");
		cte.append("	      left join material_doc_type on material_doc_type.id = material_doc.doc_type ");
		cte.append("	      left join contract_item on contract_item.contract  = contract.id  ");
		cte.append("	           and contract_item.model = material_doc_item.model_contract ");
		cte.append("  		  left join material_doc_item item_in on item_in.line_id = material_doc_item.line_id_in ");
		cte.append("	      left join material_doc item_in_doc on item_in_doc.doc_no = item_in.material_doc ");
		cte.append("          left join contract contract_in on contract_in.id = item_in.contract    ");

		if (!whereString.toString().equals("")) {
			cte.append(" where " + whereString);
		}

		if (showIncoming != null && showIncoming) {

			cte.append("    	   union ");

			cte.append("    	   select doc_type_txt, material_doc.doc_no, contract.contract_no, contract.supplier, ");
			cte.append("    	       material_doc.delivery_note,  ");
			cte.append("    	       case when material_doc_type.id=1 ");
			cte.append("    	                 then  material_doc.delivery_note else item_in_doc.delivery_note end ");
			cte.append("    	       as delivery_note_in, ");

			cte.append("    	       case when material_doc_type.id=2 ");
			cte.append("    	                 then  material_doc.delivery_note end ");
			cte.append("    	       as delivery_note_out,  ");
			cte.append("      		   contract_in.contract_no as purchase_contract_no, ");
			cte.append("    	       material_doc.doc_date, item_in_doc.plate_num, ");
			cte.append("    	       item_in_doc.batch_no, material_doc_item.model_contract, material_doc_item.model_tested, ");
			cte.append("    	       material_doc_item.net_weight*material_doc_item.direction as net_weight, ");
			cte.append("    	       material_doc_item.gross_weight, stock_location.stock_location, ");
			cte.append("    	       contract_item.unit_price, item_in_doc.working_no  ");
			cte.append("    	   from material_doc_item ");
			cte.append("    	      left join material_doc on material_doc.doc_no = material_doc_item.material_doc ");
			cte.append("              left join stock_location on stock_location.id = material_doc_item.stock_location");

			cte.append("    	      left join material_doc_type on material_doc_type.id = material_doc.doc_type ");
			cte.append("    	      left join material_doc_item item_in on item_in.line_id = material_doc_item.line_id_in ");
			cte.append("    	      left join contract on contract.id = material_doc_item.contract ");
			cte.append("    	      left join contract_item on contract_item.contract  = contract.id ");
			cte.append("    	            and contract_item.model = material_doc_item.model_contract ");

			cte.append("    	      left join material_doc item_in_doc on item_in_doc.doc_no = item_in.material_doc ");
			cte.append("              left join contract contract_in on contract_in.id = item_in.contract    ");
			cte.append("    	    where material_doc_item.line_id_in in ( select  material_doc_item.line_id ");
			cte.append("    	   from material_doc_item ");
			cte.append("    	      left join material_doc on material_doc.doc_no = material_doc_item.material_doc ");

			cte.append("    	      left join material_doc_type on material_doc_type.id = material_doc.doc_type ");

			cte.append("    	      left join material_doc_item item_in on item_in.line_id = material_doc_item.line_id_in ");
			cte.append("    	      left join material_doc item_in_doc on item_in_doc.doc_no = item_in.material_doc ");

			cte.append("    	      left join contract on contract.id = material_doc_item.contract ");
			cte.append("    	      left join contract_item on contract_item.contract  = contract.id  ");
			cte.append("    	            and contract_item.model = material_doc_item.model_contract ");

			if (!whereString.toString().equals("")) {
				cte.append(" where " + whereString);
			}

			cte.append(" ) ");

		}

		cte.append(" ), ");
		cte.append(" MaterialDocItemQuery as ( ");

		cte.append(" select (ROW_NUMBER() over (order by "
				+ sortString.toString()
				+ " )) as rowNum, * from MaterialDocItemQuery1 ");

		if (!whereOutside.toString().equals("")) {
			cte.append(" where " + whereOutside);
		}

		cte.append(" ) ");

		query.append("select * from MaterialDocItemQuery where rowNum>:start and rowNum<=:start+:limit ");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				cte.toString() + query.toString(), param);

		if (excel != null && excel.equals("true")) {
			map.put("dataRoot", "materialDocItemQuery");
			map.put("materialDocItemQuery", result);
			List<ReportHeader> headers = new ArrayList<ReportHeader>();
			ReportHeader header;
			// 合同号，供应商，规格，数量，车号，发货日期，发货地点，到达地，预计到货日期，超期天数，实际到货日期，批次号，正本，转货时间，备注
			header = new ReportHeader();

			header.setHeader("单据类型");
			header.setField("doc_type_txt");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("单据号");
			header.setField("doc_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("合同号");
			header.setField("contract_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("供应商");
			header.setField("supplier");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("规格(合同)");
			header.setField("model_contract");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("单价");
			header.setField("unit_price");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("毛重");
			header.setField("gross_weight");
			header.setFormat("#,##0.000");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("净重");
			header.setField("net_weight");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("进仓单号");
			header.setFormat("#,##0.000");
			header.setField("delivery_note_in");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("出仓单号");
			header.setField("delivery_note_out");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("车号/卡号");
			header.setField("plate_num");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("进仓日期");
			header.setField("doc_date");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("批次号");
			header.setField("batch_no");
			headers.add(header);

			header = new ReportHeader();
			header.setHeader("仓库");
			header.setField("stock_location");
			header.setAlign(org.apache.poi.hssf.usermodel.HSSFCellStyle.ALIGN_RIGHT);
			headers.add(header);

			map.put("headers", headers);
			map.put("title", "进出仓明细查询");

			return "ExportToExcel";

		} else {
			HashMap<String, Object> map2 = new HashMap<String, Object>();

			Long recordCount = jdbcTemplate.queryForLong(cte.toString()
					+ "select count(*) from MaterialDocItemQuery", param);

			map2.put("total", recordCount);
			map2.put("success", true);
			map2.put("materialDocItemQuerys", result);

			// map2.put("dataRoot", "noDeliverys");

			String resultJson = new JSONSerializer()
					.exclude("*.class")
					.include("materialDocItemQuerys")
					.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
							Date.class).serialize(map2);
			map.put("result", resultJson);

			return "resultOnly";
		}
	}
}
