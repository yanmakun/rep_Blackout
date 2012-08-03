package jp.gr.uchiwa.blackout.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author jabaraster
 */
public abstract class TableDefinition implements Serializable {
    private static final long            serialVersionUID = 8360231501722966089L;

    private final String                 tableName;
    private final List<ColumnDefinition> columns;

    /**
     * @param pTableName
     * @param pColumns
     */
    protected TableDefinition(final String pTableName, final ColumnDefinition... pColumns) {
        if (pColumns == null || pColumns.length == 0) {
            throw new IllegalArgumentException("カラム定義が１つもないテーブル定義は作れません."); //$NON-NLS-1$
        }
        this.tableName = pTableName;
        this.columns = Collections.unmodifiableList(Arrays.asList(pColumns));
    }

    /**
     * @return 全カラム名.
     */
    public String[] getAllColumnNames() {
        final String[] ret = new String[this.columns.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = this.columns.get(i).getName();
        }
        return ret;
    }

    /**
     * @return テーブル作成SQL.
     */
    public String getTableDefinitionSql() {
        final String LINE_SEPARATOR = "\n"; //$NON-NLS-1$
        final String INDENT = "  "; //$NON-NLS-1$
        final StringBuilder sql = new StringBuilder();
        sql.append("create table ").append(this.tableName).append(" ("); //$NON-NLS-1$ //$NON-NLS-2$
        sql.append(LINE_SEPARATOR) //
                .append(INDENT) //
                .append(this.columns.get(0).getDefinition());
        for (int i = 1; i < this.columns.size(); i++) {
            sql.append(LINE_SEPARATOR).append(INDENT).append(",").append(this.columns.get(i).getDefinition()); //$NON-NLS-1$
        }
        sql.append(LINE_SEPARATOR).append(")"); //$NON-NLS-1$
        return new String(sql);
    }
}