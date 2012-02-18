package com.itg.extjstest.domain;

import com.itg.extjstest.util.FilterItem;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierColumn = "lineId", identifierField = "lineId")
@RooJson
public class MaterialDocItem {

	@ManyToOne
	private MaterialDoc materialDoc;

	@Size(max = 3)
	private String moveType;

	@Size(max = 20)
	private String model_contract;

	@Size(max = 20)
	private String model_tested;

	private Double grossWeight;

	private Double netWeight;

	@Size(max = 50)
	private String warehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	private com.itg.extjstest.domain.MaterialDocItem lineId_in;

	@Size(max = 3000)
	private String remark;

	@Column(nullable = true)
	private short direction;
	
	
	
    @Transient
    private String contractNo;

    @Transient
    private String deliveryNote;

    @Transient
    private String batchNo;

    @Transient
    private String plateNum;

    @Transient
    private String workingNo;

    @Transient    
    private Date docDate;


	public static List<com.itg.extjstest.domain.MaterialDocItem> findMaterialDocItemsByFilter2(
			List<com.itg.extjstest.util.FilterItem> filters, int start,
			int page, int limit) {

		String query = "select i.lineId_in.lineId, sum(i.direction*i.netWeight)"
				+ "   from MaterialDocItem i "
				+ "   group by i.lineId_in.lineId, i.warehouse"
				+ "   having sum(i.direction*i.netWeight)>0";

		List result = entityManager().createQuery(query).getResultList();
		return result;

	}

	public static List<com.itg.extjstest.domain.MaterialDocItem> findMaterialDocItemsByFilter(
			List<com.itg.extjstest.util.FilterItem> filters, int start,
			int page, int limit) {

		CriteriaBuilder cb = entityManager().getCriteriaBuilder();

		List<Predicate> criteria = new ArrayList<Predicate>();
		CriteriaQuery<Tuple> c = cb.createTupleQuery();

		Root<MaterialDocItem> fromMaterialDocItem = c
				.from(MaterialDocItem.class);
		// Join<MaterialDocItem, MaterialDoc> joinMaterialDoc =
		// fromMaterialDocItem.join("materialDoc");
		// Join<MaterialDoc, Contract> joinContract =
		// joinMaterialDoc.join("contract");

		Expression<Double> weight = fromMaterialDocItem.get("netWeight");
		Expression<Double> direction = fromMaterialDocItem.get("direction");
		Expression<Double> directionWeight = (Expression<Double>) cb.prod(
				weight, direction);
		Expression<Double> sumOfDirectionWeight = cb.sum(directionWeight);

		c.select(cb.tuple(fromMaterialDocItem.get("lineId_in").get("lineId").alias("lineId_in"),
				fromMaterialDocItem.get("warehouse").alias("warehouse"),
				sumOfDirectionWeight.alias("stockWeight")));
		c.groupBy(fromMaterialDocItem.get("lineId_in").get("lineId"),
				fromMaterialDocItem.get("warehouse"));
		c.having(cb.gt(sumOfDirectionWeight, 0));
		

		Subquery<MaterialDocItem> subq = c.subquery(MaterialDocItem.class);
		Root<MaterialDocItem> subFromMaterialDocItem = subq
				.from(MaterialDocItem.class);
		Root<MaterialDoc> subFromMaterialDoc = subq.from(MaterialDoc.class);
		Root<Contract> subFromContract = subq.from(Contract.class);

		subq.select(subFromMaterialDocItem);
		List<Predicate> subCriteria = new ArrayList<Predicate>();

		subCriteria.add(cb.equal(subFromMaterialDocItem.get("lineId"),
				subFromMaterialDocItem.get("lineId_in")));
		subCriteria.add(cb.equal(subFromMaterialDocItem.get("materialDoc"),
				subFromMaterialDoc.get("docNo")));
		subCriteria.add(cb.equal(subFromMaterialDoc.get("contract"),
				subFromContract.get("id")));

		HashMap<String, Path> paths = new HashMap<String, Path>();
		paths.put("", fromMaterialDocItem);

		HashMap<String, Path> subPaths = new HashMap<String, Path>();
		subPaths.put("", subFromMaterialDocItem);
		subPaths.put("materialDoc", subFromMaterialDoc);
		subPaths.put("contract", subFromContract);

		if (filters != null) {
			for (FilterItem f : filters) {
				if (!f.getField().equals("warehouse")) {
					subCriteria.add(f.getPredicate(cb, subPaths));
				}else{
					criteria.add(f.getPredicate(cb, paths));
				}
			}
			subq.where(cb.and(subCriteria.toArray(new Predicate[0])));
		}
		
		c.where(cb.in(fromMaterialDocItem.get("lineId_in")).value(subq));

		List<Tuple> result = entityManager().createQuery(c)
				.setFirstResult(start).setMaxResults(limit).getResultList();
		List<MaterialDocItem> l = new ArrayList<MaterialDocItem>();
		for (Tuple o : result) {

			Long lineId = (Long) o.get("lineId_in");
			MaterialDocItem item = MaterialDocItem.findMaterialDocItem(lineId);
			MaterialDoc materialDoc = item.getMaterialDoc();
			Contract contract = materialDoc.getContract();
			item.setWarehouse((String) o.get("warehouse"));
			item.setNetWeight((Double) o.get("stockWeight"));
			entityManager().detach(item);
			l.add(item);

		}
		return l;
	}

	public static String mapToJson(
			HashMap<java.lang.String, java.lang.Object> map,
			List<com.itg.extjstest.domain.MaterialDocItem> result) {
		map.put("materialdocitems", result);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("materialdocitems")
				.include("materialdocitems.materialDoc")
				.include("materialdocitems.materialDoc.docType")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
	}
	
	public void fillLineInInfo(){
		
		if(!getLineId_in().equals(this)){
			MaterialDocItem i = getLineId_in();
			setBatchNo(i.getMaterialDoc().getBatchNo());
			setPlateNum(i.getMaterialDoc().getPlateNum());
			setDeliveryNote(i.getMaterialDoc().getDeliveryNote());
			setDocDate(i.getMaterialDoc().getDocDate());
			setWorkingNo(i.getMaterialDoc().getWorkingNo());
			setContractNo(i.getMaterialDoc().getContract().getContractNo());

		}
	}
}
