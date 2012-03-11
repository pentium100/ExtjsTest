Ext.define('AM.controller.inspection.report.InspectionDetail', {
			extend : 'Ext.app.Controller',

			views :  ['inspection.report.InspectionDetail'],
			stores : ['inspection.report.InspectionDetail'],
			models : ['inspection.report.InspectionDetail'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'InspectionDetail button[action=submit]' : {
								click : this.submitReport
							},

							'InspectionDetail button[action=exportToExcel]' : {
								click : this.exportToExcel
							}
						});

			},

			exportToExcel : function(button) {
				var panel = button.up('InspectionDetail');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				
				
				var record = panel.down('form').getValues();
				////合同类型	合同号	供应商	付款方式	备注	规格	数量	单价	备注
				var tmp = [];
				tmp = this.getFilterParam(record);
				var param;
				param = 'filter='+encodeURI(Ext.JSON.encode(tmp))+'&excel=true&start=0&limit=10000000';				
				
				window.open('reports/inspectionDetails?'+param);

				
				

			},

			submitReport : function(button) {

				var panel = button.up('InspectionDetail');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var record = button.up('form').getValues();
				tmp = this.getFilterParam(record);
				
				
				
				var p = store.getProxy();
				p.extraParams.filter = Ext.JSON.encode(tmp);
				store.load();
			},
			
			
			getFilterParam:function(record){
				var filter = {};
				var tmp = [];
				if (record.contract_no != "") {
					filter.type = "string";
					filter.field = "contract.contract_no";
					filter.value = record.contract_no;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.supplier != "") {
					filter.type = "string";
					filter.field = "contract.supplier";
					filter.value = record.supplier;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.model_contract != "") {
					filter.type = "string";
					filter.field = "materila_doc_item.model_contract";
					filter.value = record.model_contract;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.original != "") {
					filter.type = "int";
					filter.field = "inspection.original";
					filter.comparison = 'eq';
					filter.value = record.original;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.inspectionDateFrom != "") {
					filter.type = "date";
					filter.field = "inspection.inspection_date";
					filter.comparison = 'ge';
					filter.value = record.inspectionDateFrom;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.inspectionDateTo != "") {
					filter.type = "date";
					filter.field = "inspection.inspection_date";
					filter.comparison = 'le';
					filter.value = record.inspectionDateTo;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.doc_no != "") {
					filter.type = "string";
					filter.field = "inspection.doc_no";
					filter.value = record.doc_no;
					tmp.push(Ext.apply({}, filter));
				}
				
				
				if (record.plate_num != "") {
					filter.type = "string";
					filter.field = "material_doc.plate_num";
					filter.value = record.plate_num;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.batch_no != "") {
					filter.type = "string";
					filter.field = "material_doc.batch_no";
					filter.value = record.batch_no;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.delivery_note != "") {
					filter.type = "string";
					filter.field = "material_doc.delivery_note";
					filter.value = record.delivery_note;
					tmp.push(Ext.apply({}, filter));
				}
				
				
				return tmp;
                
				
				
			}
		});