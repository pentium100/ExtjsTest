

Ext.require(['AM.ux.grid.feature.RemoteSummary']);
Ext.define('AM.controller.contract.report.ContractHistory', {
			extend : 'Ext.app.Controller',

			views :  ['contract.report.ContractHistory'],
			stores : ['contract.report.ContractHistory', 'master.employee.Employees'],
			models : ['contract.report.ContractHistory'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'ContractHistory button[action=submit]' : {
								click : this.submitReport
							},

							'ContractHistory button[action=exportToExcel]' : {
								click : this.exportToExcel
							}
						});

			},

			exportToExcel : function(button) {
				var panel = button.up('ContractHistory');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				
				
				var record = panel.down('form').getValues();
				////合同类型	合同号	供应商	付款方式	备注	规格	数量	单价	备注
				var tmp = [];
				tmp = this.getFilterParam(record);
				var param;
				param = 'filter='+encodeURI(Ext.JSON.encode(tmp))+'&excel=true&start=0&limit=10000000';				
				
				window.open('reports/contractHistorys?'+param);

				
				

			},

			submitReport : function(button) {

				var panel = button.up('ContractHistory');
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
					filter.field = "contract_no";
					filter.value = record.contract_no;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.contract_type != "" && record.contract_type != undefined) {
					filter.type = "list";
					filter.field = "contract_type";
					filter.value = record.contract_type;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.supplier != "") {
					filter.type = "string";
					filter.field = "supplier";
					filter.value = record.supplier;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.signDateFrom != "") {
					filter.type = "date";
					filter.field = "contract.sign_date";
					filter.comparison = 'ge';
					filter.value = record.signDateFrom;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.signDateTo != "") {
					filter.type = "date";
					filter.field = "contract.sign_date";
					filter.comparison = 'le';
					filter.value = record.signDateTo;
					tmp.push(Ext.apply({}, filter));
				}
				
				
				if (record.pay_term != "") {
					filter.type = "string";
					filter.field = "pay_term";
					filter.value = record.pay_term;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.model != "") {
					filter.type = "string";
					filter.field = "model";
					filter.value = record.model;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.remark != "") {
					filter.type = "string";
					filter.field = "contract.remark";
					filter.value = record.remark;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.item_remark != "") {
					filter.type = "string";
					filter.field = "contract_item.remark";
					filter.value = record.item_remark;
					tmp.push(Ext.apply({}, filter));
				}				
				if (record.employee != undefined && record.employee != '') {
					filter.type = "string";
					filter.field = "employee.id";
					filter.value = record.employee;
					tmp.push(Ext.apply({}, filter));
				}
				return tmp;
                
				
				
			}
		});