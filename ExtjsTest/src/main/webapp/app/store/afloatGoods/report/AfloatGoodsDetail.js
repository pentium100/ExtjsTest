

Ext.define('AM.store.afloatGoods.report.AfloatGoodsDetail', {
			extend : 'Ext.data.ux.Store',
			model : 'AM.model.afloatGoods.report.AfloatGoodsDetail',
			remoteSort: true,

			proxy : {
				type : 'rest',
				url : 'reports/afloatGoodsDetails',
				reader : {
					type : 'json',
					root : 'afloatGoodsDetails',
					successProperty : 'success'
				}
			}
		});
