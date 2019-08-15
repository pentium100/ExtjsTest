Ext.require(['AM.ux.grid.feature.RemoteSummary']);
Ext.define('AM.controller.materialDoc.report.MaterialDocItemQuerys', {
			extend : 'Ext.app.Controller',

			views : ['materialDoc.report.MaterialDocItemQuery'],
			stores : ['materialDoc.report.MaterialDocItemQuery',
					'master.employee.Employees'],
			models : ['materialDoc.report.MaterialDocItemQuery',
					'master.employee.Employee'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'MaterialDocItemQuery button[action=submit]' : {
								click : this.submitReport
							},

							'MaterialDocItemQuery button[action=exportToExcel]' : {
								click : this.exportToExcel
							}
						});

			},

			exportToExcel : function(button) {
				var panel = button.up('MaterialDocItemQuery');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var record = panel.down('form').getValues();
				// //合同类型 合同号 供应商 付款方式 备注 规格 数量 单价 备注
				var tmp = [];
				tmp = this.getFilterParam(record);
				var param;

				if (record.showIncoming == undefined) {
					record.showIncoming = false;
				}
				param = 'filter=' + encodeURI(Ext.JSON.encode(tmp))
						+ '&excel=true&start=0&limit=10000000&showIncoming='
						+ record.showIncoming;

				window.open('reports/materialDocItemQuerys?' + param);

			},

			submitReport : function(button) {

				var panel = button.up('MaterialDocItemQuery');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var record = button.up('form').getValues();
				tmp = this.getFilterParam(record);

				if (record.showIncoming == undefined) {
					record.showIncoming = false;
				}

				var p = store.getProxy();
				p.extraParams.filter = Ext.JSON.encode(tmp);
				p.extraParams.showIncoming = record.showIncoming;
				store.currentPage = 1;
				store.load();
			},

			getFilterParam : function(record) {
				var filter = {};
				var tmp = [];
				if (record.contract_no != "") {
					filter.type = "string";
					filter.comparison = '';
					filter.field = "contract.contract_no";
					filter.value = record.contract_no;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.doc_type_txt != undefined
						&& record.doc_type_txt != "") {
					filter.type = "string";
					filter.comparison = '';
					filter.field = "doc_type_txt";
					filter.value = record.doc_type_txt;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.employee != undefined && record.employee != "") {
					filter.type = "int";
					filter.field = "contract.employee";
					filter.comparison = 'eq';
					
					filter.value = record.employee;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.supplier != "") {
					filter.type = "string";
					filter.field = "contract.supplier";
					filter.comparison = '';
					filter.value = record.supplier;
					tmp.push(Ext.apply({}, filter));
				}

				
				if (record.cause != "") {
					filter.type = "string";
					filter.field = "material_doc.cause";
					filter.comparison = '';
					filter.value = record.cause;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.working_no != "") {
					filter.type = "string";
					filter.field = "material_doc.working_no";
					filter.comparison = '';
					filter.value = record.working_no;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.model_contract != "") {
					filter.type = "string";
					filter.field = "material_doc_item.model_contract";
					filter.comparison = '';
					filter.value = record.model_contract;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.inv_no != "") {
					filter.type = "string";
					filter.field = "material_doc.inv_no";
					filter.comparison = '';
					filter.value = record.inv_no;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.docDateFrom != "") {
					filter.type = "date";
					filter.comparison = 'ge';
					filter.field = "material_doc.doc_date";
					filter.value = record.docDateFrom;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.docDateTo != "") {
					filter.type = "date";
					filter.comparison = 'le';
					filter.field = "material_doc.doc_date";
					filter.value = record.docDateTo;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.stock_location != "") {
					filter.type = "string";
					filter.field = "stock_location.stock_location";
					filter.comparison = '';
					filter.value = record.stock_location;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.delivery_note != "") {
					filter.type = "string";
					filter.comparison = '';
					filter.field = "material_doc.delivery_note";

					filter.value = record.delivery_note;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.plate_num != "") {
					filter.type = "string";
					filter.field = "item_in_doc.plate_num";
					filter.comparison = '';
					filter.value = record.plate_num;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.batch_no != "") {
					filter.type = "string";
					filter.field = "item_in_doc.batch_no";
					filter.comparison = '';
					filter.value = record.batch_no;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.materialItemRemark != "") {
					filter.type = "string";
					filter.field = "material_doc_item.remark";
					filter.comparison = '';
					filter.value = record.materialItemRemark;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.inspection_remark != "") {
					filter.type = "string";
					filter.field = "insp.remark";
					filter.comparison = '';
					filter.value = record.inspection_remark;
					tmp.push(Ext.apply({}, filter));
				}

				return tmp;

			}
		});