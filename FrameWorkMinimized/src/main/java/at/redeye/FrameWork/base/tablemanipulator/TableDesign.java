package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.BaseDialogBase;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class TableDesign extends AbstractTableModel {

    public Set<Integer> edited_cols = new HashSet<>();
    public Set<Integer> edited_rows = new HashSet<>();
    public List<List<?>> rows = new ArrayList<>();
    private final BaseDialogBase base;

    public List<Coll> colls;

    public TableDesign(BaseDialogBase base, List<Coll> colls) {
        this.base = base;
        this.colls = colls;
    }

    void clear() {
        edited_cols.clear();
        edited_rows.clear();
        rows.clear();
    }

    public List<String> getAllOfCollSorted(int col) {
        Coll coll = null;

        if (col >= 0 && col < colls.size()) {
            coll = colls.get(col);
        }

        if (coll == null || !coll.getDoAutocompleteForAllOfThisColl())
            return emptyList();

        return rows.stream()
                .filter(row -> row.size() > col && col > 0)
                .map(row -> row.get(col))
                .map(Object::toString)
                .sorted()
                .collect(toList());
    }


    static int getModelCol( JTable table, int col )
    {
        return table.getColumnModel().getColumn(col).getModelIndex();
    }

    static int getModelRow(JTable table, int row) {
        RowSorter<?> sorter = table.getRowSorter();

        if (sorter == null)
            return row;

        return sorter.convertRowIndexToModel(row);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return colls.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return base.MlM(colls.get(column).title);
    }

    @Override
    public boolean isCellEditable(int rowindex, int columnindex) {
        return colls.get(columnindex).isEditable;
    }
}
