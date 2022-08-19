package model;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//generic table load from generic list array
public class JTableLoader <T> {

    public final JTable table;
    private List<T> data;
    private Class<T> entity;
    private DefaultTableModel model;
    private String[] bindedProperties = null;

    public JTableLoader(JTable table) {
        this(table, null, null);
    }

    public JTableLoader(JTable table, String propertiesToBind) {
        this(table, propertiesToBind, null);

    }

    public JTableLoader(JTable table, String propertiesToBind, String[] tableColumns) {
        this.table = table;

        // Check if property has been set
        if (propertiesToBind != null && !propertiesToBind.isEmpty()) {
            this.bindedProperties = propertiesToBind.split(",");
        }

        // Check if columns of table is set on this constructor
        if (tableColumns == null) {
            // not set, it means the table has a model
            this.model = (DefaultTableModel) table.getModel();
        } else {
            // set, it means the table has no model then create model and set to table
            this.model = new DefaultTableModel(null, tableColumns);
            table.setModel(this.model);
        }
    }

    /**
     * Bind the field and get method name (without parameter) in T class to show
     * in table column separate by comma. ex: "id,name"
     *
     * @param propertiesToBind fields or(and) methods name
     */
    public void bindPropertyNames(String propertiesToBind) {
        this.bindedProperties = propertiesToBind.split(",");
    }

    /**
     * Load the list of T class
     *
     * @param list data to load
     */
    public void load(List<T> list) {
        // Get selected row table
        int row = table.getSelectedRow();

        // Get total row count in table
        int count = table.getRowCount();

        // Clear rows in the table
        model.setRowCount(0);

        // Set the list
        this.data = list;
        if (data == null || data.isEmpty()) {
            // if data null or empty then stop the load process
            return;
        }

        // Get type class of entity property
        entity = (Class<T>) data.get(0).getClass();

        // Initialize property to set in table by binded property name or by field name from entity class
        String[] field = bindedProperties == null ? getFieldNames(entity) : bindedProperties;

        // Loop the list data and set it to row table model
        data.forEach(t -> model.addRow(values(t, field)));

        // Set row selection to current position
        if (row != -1) {
            if (count == table.getRowCount()) {
                table.setRowSelectionInterval(row, row);
            }
        }
    }

    private Object[] values(T t, String[] field) {
        Object[] values = new Object[field.length];
        for (int i = 0; i < field.length; i++) {
            values[i] = getValue(t, field[i]);
        }
        return values;
    }

    // Get data from selected row
    public T getSelectedItem() {
        return data.get(table.convertRowIndexToModel(table.getSelectedRow()));
    }

    // Get array item from selected rows
    public T[] getSelectedItems() {
        if (data.isEmpty()) {
            return null;
        }
        int[] rows = table.getSelectedRows();
        //noinspection unchecked
        T[] d = (T[]) Array.newInstance(entity, rows.length);
        for (int i = 0; i < rows.length; i++) {
            d[i] = data.get(table.convertRowIndexToModel(rows[i]));
        }
        return d;
    }

    // Get list item from selected rows
    public List<T> listSelectedItem() {
        int[] rows = table.getSelectedRows();
        List<T> d = new ArrayList<>();
        for (int r : rows) {
            d.add(data.get(table.convertRowIndexToModel(r)));
        }
        return d;
    }

    // Get all list item
    public List<T> getItems() {
        return data;
    }

    // Check if field exists in class
    private boolean fieldExists(T t, String fieldName) {
        return Arrays.stream(t.getClass().getDeclaredFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }

    // Get value in class by field name
    private Object getValue(T t, String field) throws RuntimeException {
        Class<T> bean = (Class<T>) t.getClass();
        try {
            if (fieldExists(t, field)) {
                final Field f = bean.getDeclaredField(field);
                f.setAccessible(true);
                return (T) f.get(t);
            } else {
                final Method m = bean.getDeclaredMethod(field);
                m.setAccessible(true);
                return (T) m.invoke(t);
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException
                 | SecurityException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Get all field name
    private String[] getFieldNames(Class<T> bean) {
        String[] fieldName;
        Field[] fs = bean.getDeclaredFields();
        fieldName = new String[fs.length];
        for (int i = 0; i < fs.length; i++) {
            fieldName[i] = fs[i].getName();
        }
        return fieldName;
    }

    public void removeSelectedRow() {
        model.removeRow(table.getSelectedRow());
    }
}
