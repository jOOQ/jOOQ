package org.jooq.debug.console.misc;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * XTableColumnModel extends the DefaultTableColumnModel .
 * It provides a comfortable way to hide/show columns.
 * Columns keep their positions when hidden and shown again.
 *
 * In order to work with JTable it cannot add any events to TableColumnModelListener.
 * Therefore hiding a column will result in columnRemoved event and showing it
 * again will notify listeners of a columnAdded, and possibly a columnMoved event.
 * For the same reason the following methods still deal with visible columns only:
 * getColumnCount(), getColumns(), getColumnIndex(), getColumn()
 * There are overloaded versions of these methods that take a parameter onlyVisible which let's
 * you specify whether you want invisible columns taken into account.
 *
 * @version 0.9 04/03/01
 * @author Stephen Kelvin, mail@StephenKelvin.de
 * @see DefaultTableColumnModel
 */

public class XTableColumnModel extends DefaultTableColumnModel {
	/** Array of TableColumn objects in this model.
	 *  Holds all column objects, regardless of their visibility
	 */
	protected Vector<TableColumn> allTableColumns = new Vector<TableColumn>();

	/**
	 * Creates an extended table column model.
	 */
	public XTableColumnModel() {
	}

	/**
	 * Sets the visibility of the specified TableColumn.
	 * The call is ignored if the TableColumn is not found in this column model
	 * or its visibility status did not change.
	 * @param aColumn        the column to show/hide
	 * @param visible its new visibility status
	 */
	// listeners will receive columnAdded()/columnRemoved() event
	public void setColumnVisible(TableColumn column, boolean visible) {
		if(!visible) {
			super.removeColumn(column);
		}
		else {
			// find the visible index of the column:
			// iterate through both collections of visible and all columns, counting
			// visible columns up to the one that's about to be shown again
			int noVisibleColumns = tableColumns.size();
			int noInvisibleColumns = allTableColumns.size();
			int visibleIndex = 0;

			for(int invisibleIndex = 0; invisibleIndex < noInvisibleColumns; ++invisibleIndex) {
				TableColumn visibleColumn = (visibleIndex < noVisibleColumns ? (TableColumn)tableColumns.get(visibleIndex) : null);
				TableColumn testColumn = allTableColumns.get(invisibleIndex);

				if(testColumn == column) {
					if(visibleColumn != column) {
						super.addColumn(column);
						super.moveColumn(tableColumns.size() - 1, visibleIndex);
					}
					return; // ####################
				}
				if(testColumn == visibleColumn) {
					++visibleIndex;
				}
			}
		}
	}

	/**
	 * Makes all columns in this model visible
	 */
	public void setAllColumnsVisible() {
		int noColumns = allTableColumns.size();

		for(int columnIndex = 0; columnIndex < noColumns; ++columnIndex) {
			TableColumn visibleColumn = (columnIndex < tableColumns.size() ? (TableColumn)tableColumns.get(columnIndex) : null);
			TableColumn invisibleColumn = allTableColumns.get(columnIndex);

			if(visibleColumn != invisibleColumn) {
				super.addColumn(invisibleColumn);
				super.moveColumn(tableColumns.size() - 1, columnIndex);
			}
		}
	}

	/**
	 * Maps the index of the column in the table model at
	 * modelColumnIndex to the TableColumn object.
	 * There may me multiple TableColumn objects showing the same model column, though this is uncommon.
	 * This method will always return the first visible or else the first invisible column with the specified index.
	 * @param modelColumnIndex index of column in table model
	 * @return table column object or null if no such column in this column model
	 */
	public TableColumn getColumnByModelIndex(int modelColumnIndex) {
		for (int columnIndex = 0; columnIndex < allTableColumns.size(); ++columnIndex) {
			TableColumn column = allTableColumns.elementAt(columnIndex);
			if(column.getModelIndex() == modelColumnIndex) {
				return column;
			}
		}
		return null;
	}

	/** Checks whether the specified column is currently visible.
	 * @param aColumn column to check
	 * @return visibility of specified column (false if there is no such column at all. [It's not visible, right?])
	 */
	public boolean isColumnVisible(TableColumn aColumn) {
		return (tableColumns.indexOf(aColumn) >= 0);
	}

	/** Append column to the right of existing columns.
	 * Posts columnAdded event.
	 * @param column The column to be added
	 * @see #removeColumn
	 * @exception IllegalArgumentException if column is null
	 */
	@Override
	public void addColumn(TableColumn column) {
		allTableColumns.addElement(column);
		super.addColumn(column);
	}

	/** Removes column from this column model.
	 * Posts columnRemoved event.
	 * Will do nothing if the column is not in this model.
	 * @param column the column to be added
	 * @see #addColumn
	 */
	@Override
	public void removeColumn(TableColumn column) {
		int allColumnsIndex = allTableColumns.indexOf(column);
		if(allColumnsIndex != -1) {
			allTableColumns.removeElementAt(allColumnsIndex);
		}
		super.removeColumn(column);
	}

	/** Moves the column from oldIndex to newIndex.
	 * Posts  columnMoved event.
	 * Will not move any columns if oldIndex equals newIndex.
	 *
	 * @param    oldIndex            index of column to be moved
	 * @param    newIndex            new index of the column
	 * @exception IllegalArgumentException    if either oldIndex or
	 *                         newIndex
	 *                        are not in [0, getColumnCount() - 1]
	 */
	@Override
	public void moveColumn(int oldIndex, int newIndex) {
		if ((oldIndex < 0) || (oldIndex >= getColumnCount()) ||
				(newIndex < 0) || (newIndex >= getColumnCount())) {
			throw new IllegalArgumentException("moveColumn() - Index out of range");
		}

		TableColumn fromColumn = tableColumns.get(oldIndex);
		TableColumn toColumn = tableColumns.get(newIndex);

		int allColumnsOldIndex = allTableColumns.indexOf(fromColumn);
		int allColumnsNewIndex = allTableColumns.indexOf(toColumn);

		if(oldIndex != newIndex) {
			allTableColumns.removeElementAt(allColumnsOldIndex);
			allTableColumns.insertElementAt(fromColumn, allColumnsNewIndex);
		}

		super.moveColumn(oldIndex, newIndex);
	}

	/**
	 * Returns the total number of columns in this model.
	 *
	 * @param   onlyVisible   if set only visible columns will be counted
	 * @return    the number of columns in the tableColumns array
	 * @see    #getColumns
	 */
	public int getColumnCount(boolean onlyVisible) {
		Vector<TableColumn> columns = (onlyVisible ? tableColumns : allTableColumns);
		return columns.size();
	}

	/**
	 * Returns an Enumeration of all the columns in the model.
	 *
	 * @param   onlyVisible   if set all invisible columns will be missing from the enumeration.
	 * @return an Enumeration of the columns in the model
	 */
	public Enumeration<TableColumn> getColumns(boolean onlyVisible) {
		Vector<TableColumn> columns = (onlyVisible ? tableColumns : allTableColumns);

		return columns.elements();
	}

	/**
	 * Returns the position of the first column whose identifier equals identifier.
	 * Position is the the index in all visible columns if onlyVisible is true or
	 * else the index in all columns.
	 *
	 * @param    identifier   the identifier object to search for
	 * @param    onlyVisible  if set searches only visible columns
	 *
	 * @return        the index of the first column whose identifier
	 *            equals identifier
	 *
	 * @exception       IllegalArgumentException  if identifier
	 *                is null, or if no
	 *                TableColumn has this
	 *                identifier
	 * @see        #getColumn
	 */
	public int getColumnIndex(Object identifier, boolean onlyVisible) {
		if (identifier == null) {
			throw new IllegalArgumentException("Identifier is null");
		}

		Vector<TableColumn> columns = (onlyVisible ? tableColumns : allTableColumns);
		int noColumns = columns.size();
		TableColumn column;

		for(int columnIndex = 0; columnIndex < noColumns; ++columnIndex) {
			column = columns.get(columnIndex);

			if(identifier.equals(column.getIdentifier())) {
				return columnIndex;
			}
		}

		throw new IllegalArgumentException("Identifier not found");
	}

	/**
	 * Returns the TableColumn object for the column at columnIndex.
	 * @param    columnIndex    the index of the column desired
	 * @param    onlyVisible    if set columnIndex is meant to be relative to all visible columns only else it is the index in all columns
	 * @return    the TableColumn object for the column at columnIndex
	 */
	public TableColumn getColumn(int columnIndex, boolean onlyVisible) {
		return tableColumns.elementAt(columnIndex);
	}
}
