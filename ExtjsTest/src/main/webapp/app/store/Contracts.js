

Ext.define('AM.store.Contracts', {
			extend : 'Ext.data.Store',
			model : 'AM.model.Contract',

			// data: [
			// {contractType: 'SD', eta: '2011-12-1', contractNo:'100000-1',
			// contractItems:[{model:'xxx1', quantity:120},{model:'xxx2',
			// quantity:240},{model:'xxx3', quantity:360}]
			// },
			// {contractType: 'SD', eta: '2011-12-2', contractNo:'100000-2',
			// contractItems:[{model:'yyy1', quantity:120}, {model:'yyy2',
			// quantity:240},{model:'yyy3', quantity:360}]
			// }

			// ]
			autoLoad : true,
			proxy : {
				type : 'ajax',
				url : 'contracts',
				reader : {
					type : 'json',
					root : 'contracts',
					successProperty : 'success'
				},

				writer : {
					type : 'json',
					writeAllFields : true,
					root : ''
				}
			}
		});
		
		
Ext.data.writer.Json.override({
	/*
	 * This function overrides the default implementation of json writer. Any
	 * hasMany relationships will be submitted as nested objects. When preparing
	 * the data, only children which have been newly created, modified or marked
	 * for deletion will be added. To do this, a depth first bottom -> up
	 * recursive technique was used.
	 */
	getRecordData : function(record) {
		// Setup variables
		var me = this, i, association, childStore, data = {};
		if (record.proxy.writer.writeAllFields) {
			data = record.data;
		} else {
			var changes, name, field, fields = record.fields, nameProperty = this.nameProperty, key;
			changes = record.getChanges();
			for (key in changes) {
				if (changes.hasOwnProperty(key)) {
					field = fields.get(key);
					name = field[nameProperty] || field.name;
					data[name] = changes[key];
				}
			}
			if (!record.phantom) {
				// always include the id for non phantoms
				data[record.idProperty] = record.getId();
			}
		}

		// Iterate over all the hasMany associations
		for (i = 0; i < record.associations.length; i++) {
			association = record.associations.get(i);
			data[association.name] = [];
			childStore = record[association.storeName];

			// Iterate over all the children in the current association
			childStore.each(function(childRecord) {

						// Recursively get the record data for children (depth
						// first)
						var childData = this.getRecordData.call(this,
								childRecord);

						/*
						 * If the child was marked dirty or phantom it must be
						 * added. If there was data returned that was neither
						 * dirty or phantom, this means that the depth first
						 * recursion has detected that it has a child which is
						 * either dirty or phantom. For this child to be put
						 * into the prepared data, it's parents must be in place
						 * whether they were modified or not.
						 */
						if (childRecord.dirty | childRecord.phantom
								| (childData != null)) {
							data[association.name].push(childData);
							record.setDirty();
						}
					}, me);

			/*
			 * Iterate over all the removed records and add them to the
			 * preparedData. Set a flag on them to show that they are to be
			 * deleted
			 */
			Ext.each(childStore.removed, function(removedChildRecord) {
						// Set a flag here to identify removed records
						removedChildRecord.set('forDeletion', true);
						var removedChildData = this.getRecordData.call(this,
								removedChildRecord);
						data[association.name].push(removedChildData);
						record.setDirty();
					}, me);
		}

		// Only return data if it was dirty, new or marked for deletion.
		if (record.dirty | record.phantom | record.get('forDeletion')) {
			return data;
		}
	}
});

Ext.data.Store.override({
	remove : function(records, /* private */isMove) {
		if (!Ext.isArray(records)) {
			records = [records];
		}

		/*
		 * Pass the isMove parameter if we know we're going to be re-inserting
		 * this record
		 */
		isMove = isMove === true;
		var me = this, sync = false, i = 0, length = records.length, isPhantom, index, record;

		for (; i < length; i++) {
			record = records[i];
			index = me.data.indexOf(record);

			if (me.snapshot) {
				me.snapshot.remove(record);
			}

			if (index > -1) {
				isPhantom = record.phantom === true;
				if (!isMove && !isPhantom) {
					// don't push phantom records onto removed
					record.set('forDeletion', true);
					me.removed.push(record);
				}

				record.unjoin(me);
				me.data.remove(record);
				sync = sync || !isPhantom;

				me.fireEvent('remove', me, record, index);
			}
		}

		me.fireEvent('datachanged', me);
		if (!isMove && me.autoSync && sync) {
			me.sync();
		}
	}
});
Ext.data.TreeStore.override({
			onNodeRemove : function(parent, node) {
				var removed = this.removed;

				if (!node.isReplace && Ext.Array.indexOf(removed, node) == -1) {
					node.set('forDeletion', true);
					removed.push(node);
				}
			}
		});

		