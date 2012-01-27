Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.ux', 'js/extjs4/examples/ux');
Ext.require(['Ext.form.field.Trigger']);
Ext.require(['Ext.data.writer.Json', 'Ext.data.Store', 'Ext.data.TreeStore',
				'Ext.ux.grid.menu.ListMenu'], function() {
			//
			Ext.data.writer.Json.override({
				/*
				 * This function overrides the default implementation of json
				 * writer. Any hasMany relationships will be submitted as nested
				 * objects. When preparing the data, only children which have
				 * been newly created, modified or marked for deletion will be
				 * added. To do this, a depth first bottom -> up recursive
				 * technique was used.
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

						// Iterate over all the children in the current
						// association
						childStore.each(function(childRecord) {

									// Recursively get the record data for
									// children (depth
									// first)
									childRecord.setDirty(true);
									var childData = this.getRecordData.call(
											this, childRecord);

									/*
									 * If the child was marked dirty or phantom
									 * it must be added. If there was data
									 * returned that was neither dirty or
									 * phantom, this means that the depth first
									 * recursion has detected that it has a
									 * child which is either dirty or phantom.
									 * For this child to be put into the
									 * prepared data, it's parents must be in
									 * place whether they were modified or not.
									 */
									if (childRecord.dirty | childRecord.phantom
											| (childData != null)) {
										data[association.name].push(childData);
										record.setDirty();
									}
								}, me);

						/*
						 * Iterate over all the removed records and add them to
						 * the preparedData. Set a flag on them to show that
						 * they are to be deleted
						 */
						Ext.each(childStore.removed, function(
										removedChildRecord) {
									// Set a flag here to identify removed
									// records
									removedChildRecord.set('forDeletion', true);
									var removedChildData = this.getRecordData
											.call(this, removedChildRecord);
									data[association.name]
											.push(removedChildData);
									record.setDirty();
								}, me);
					}

					// Only return data if it was dirty, new or marked for
					// deletion.
					if (record.dirty | record.phantom
							| record.get('forDeletion')) {
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
					 * Pass the isMove parameter if we know we're going to be
					 * re-inserting this record
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

			Ext.data.Store.override({
				filterUpdated : function(item) {
					// only want dirty records, not phantoms that are valid
					var itemUpdated = false;
					var masterUpdated = item.dirty === true
							&& item.phantom !== true && item.isValid();
					if (!masterUpdated) {

						for (i = 0; i < item.associations.length; i++) {
							var association = item.associations.get(i);
							// data[association.name] = [];
							var childStore = item[association.storeName];

							// Iterate over all the children in the current
							// association
							var toCreate = childStore.getNewRecords(), toUpdate = childStore
									.getUpdatedRecords(), toDestroy = childStore
									.getRemovedRecords();
							if (toCreate.length > 0 || toUpdate.length > 0
									|| toDestroy.length > 0) {
								itemUpdated = true;
							}

						}
					}

					return masterUpdated || itemUpdated;
				}

			});

			Ext.data.TreeStore.override({
						onNodeRemove : function(parent, node) {
							var removed = this.removed;

							if (!node.isReplace
									&& Ext.Array.indexOf(removed, node) == -1) {
								node.set('forDeletion', true);
								removed.push(node);
							}
						}
					});

			Ext.data.Store.override({
						rejectChanges : function() {
							var me = this;

							// re-add removed records
							Ext.each(me.removed, function(rec) {
										rec.join(me);
										me.data.add(rec);
									});
							me.removed = [];

							// revert dirty records and trash newly added
							// records
							// ('phantoms')
							me.each(function(rec) {
										if (rec.dirty) {
											rec.reject();
										}
										if (rec.phantom) {
											record.unjoin(me); // probably not
											// really
											// necessary
											me.data.remove(rec);
										}
									});

							me.fireEvent('datachanged', me);
						}
					});
					
			Ext.ux.grid.menu.ListMenu.override({
						show : function() {
							var lastArgs = null;
							return function() {
								lastArgs = arguments;
								if (this.loadOnShow && !this.loaded) {
									this.store.load();
								}
								this.callParent(arguments);

							}
						}()
					});
					

		});

Ext.JSON.encodeDate = function(d) {
	return Ext.Date.format(d, '"Y-m-d H:i:s"');
};


