package jp.gr.uchiwa.blackout.service;

import java.io.Serializable;

/**
 * @author jabaraster
 */
public class ColumnDefinition implements Serializable {
    private static final long serialVersionUID = 5060647426576914946L;

    private final String      name;
    private final ColumnType  type;
    private final boolean     primaryKey;
    private final boolean     nullable;

    /**
     * @param pName
     * @param pType
     * @param pPrimaryKey
     * @param pNullable
     */
    public ColumnDefinition(final String pName, final ColumnType pType, final boolean pPrimaryKey, final boolean pNullable) {
        this.name = pName;
        this.type = pType;
        this.primaryKey = pPrimaryKey;
        this.nullable = pNullable;
    }

    /**
     * @return DDL文に使えるカラム定義. <br>
     *         "カラム名 型名"の書式.
     */
    public String getDefinition() {
        return this.name + " " + this.type.getType() // //$NON-NLS-1$
                + (this.primaryKey ? " primary key" : "") //  //$NON-NLS-1$//$NON-NLS-2$
                + (this.nullable ? "" : " not null"); //$NON-NLS-1$ //$NON-NLS-2$ 
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the type
     */
    public ColumnType getType() {
        return this.type;
    }

    /**
     * @return the nullable
     */
    public boolean isNullable() {
        return this.nullable;
    }

    /**
     * @return the primaryKey
     */
    public boolean isPrimaryKey() {
        return this.primaryKey;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * @param pName
     * @return -
     */
    public static DateColumnDefinition date(final String pName) {
        return new DateColumnDefinition(pName, false, "yyyy/MM/dd"); //$NON-NLS-1$
    }

    /**
     * @param pName
     * @return -
     */
    public static DateColumnDefinition datetime(final String pName) {
        return new DateColumnDefinition(pName, false, "yyyy/MM/dd HH:mm:ss"); //$NON-NLS-1$
    }

    /**
     * @param pName
     * @return カラム定義.
     */
    public static ColumnDefinition integer(final String pName) {
        return new ColumnDefinition(pName, ColumnType.INTEGER, false, false);
    }

    /**
     * @param pName
     * @return カラム定義.
     */
    public static ColumnDefinition primaryKey(final String pName) {
        return new ColumnDefinition(pName, ColumnType.INTEGER, true, false);
    }

    /**
     * @param pName
     * @return カラム定義.
     */
    public static ColumnDefinition text(final String pName) {
        return new ColumnDefinition(pName, ColumnType.TEXT, false, false);
    }

    /**
     * @param pName
     * @return -
     */
    public static DateColumnDefinition time(final String pName) {
        return new DateColumnDefinition(pName, false, "HH:mm"); //$NON-NLS-1$
    }

    /**
     * @author jabaraster
     */
    public enum ColumnType {
        /**
         * 
         */
        TEXT("TEXT"), //$NON-NLS-1$

        /**
         * 
         */
        INTEGER("INTEGER"), //$NON-NLS-1$

        ;

        private final String type;

        ColumnType(final String pType) {
            this.type = pType;
        }

        /**
         * @return 型.
         */
        public String getType() {
            return this.type;
        }
    }
}