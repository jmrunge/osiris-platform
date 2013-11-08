/*******************************************************************************
 * Copyright (c) 1998, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package ar.com.zir.osiris.ejb.persistence;

import java.io.StringWriter;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.internal.databaseaccess.DatabasePlatform;
import org.eclipse.persistence.internal.helper.DatabaseTable;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.ValueReadQuery;
import org.eclipse.persistence.sessions.Session;

/**
 * <p>
 * <b>Purpose</b>: Defines sequencing through using a SEQUENCE table.
 * <p>
 * <b>Description</b>
 * This is the default sequencing mechanism.
 * A table defaulting to SEQUENCE is used to generate unique ids.
 * The table has a name field (SEQ_NAME) storing each sequences name,
 * and a counter (SEQ_COUNT) storing the last sequence id generated.
 * There will be a row in the table for each sequence object.
 */
public class TableSequence extends QuerySequence implements SessionCustomizer {
    /** Default sequence table name
     * @deprecated 
     * Use an empty string as a default sequence table name instead, 
     * that triggers usage of platform.getDefaultSequenceTableName() when the sequence is connected.
     */    
    public static final String defaultTableName = "SEQUENCE";

    /** Hold the database table */
    protected DatabaseTable table;

    /** Hold the name of the column in the sequence table which specifies the sequence numeric value */
    protected String counterFieldName = "SEQ_COUNT";

    /** Hold the name of the column in the sequence table which specifies the sequence name */
    protected String nameFieldName = "SEQ_NAME";
    
    public TableSequence() {
        super(false, true);
        setTableName("");
    }
    
    /**
     * Create a new sequence with the name.
     */
    public TableSequence(String name) {
        super(name, false, true);
        setTableName("");
    }

    /**
     * Create a new sequence with the name and sequence pre-allocation size.
     */
    public TableSequence(String name, int size) {
        super(name, size, false, true);
        setTableName("");
    }
    
    public TableSequence(String name, int size, int initialValue) {
        super(name, size, initialValue, false, true);
        setTableName("");
    }
    
    /**
     * Create a new sequence with the name, and the sequence table name.
     */
    public TableSequence(String name, String tableName) {
        this(name);
        setTableName(tableName);
    }
    
    /**
     * Create a new sequence with the name, and the sequence table information.
     */
    public TableSequence(String name, String tableName, String nameFieldName, String counterFieldName) {
        this(name);
        setTableName(tableName);
        setNameFieldName(nameFieldName);
        setCounterFieldName(counterFieldName);
    }

    public TableSequence(String name, int size, String tableName) {
        this(name, size);
        setTableName(tableName);
    }

    public TableSequence(String name, int size, String tableName, String nameFieldName, String counterFieldName) {
        this(name, size);
        setTableName(tableName);
        setNameFieldName(nameFieldName);
        setCounterFieldName(counterFieldName);
    }

    public boolean isTable() {
        return true;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof TableSequence) {
            TableSequence other = (TableSequence)obj;
            if (equalNameAndSize(this, other)) {
                return getTableName().equals(other.getTableName()) && getCounterFieldName().equals(other.getCounterFieldName()) && getNameFieldName().equals(other.getNameFieldName());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getCounterFieldName() {
        return counterFieldName;
    }

    public void setCounterFieldName(String name) {
        counterFieldName = name;
    }

    public String getNameFieldName() {
        return nameFieldName;
    }

    public void setNameFieldName(String name) {
        nameFieldName = name;
    }

    public DatabaseTable getTable() {
        return table;
    }
    
    public String getTableName() {
        return getTable().getQualifiedName();
    }

    public String getQualifiedTableName() {
        return getQualified(getTableName());
    }

    public void setTable(DatabaseTable table) {
        this.table = table;
    }
    
    public void setTableName(String name) {
        table = new DatabaseTable(name);
    }
    
    public void onConnect() {
        if(this.table.getName().length() == 0) {
            this.table.setName(((DatabasePlatform)getDatasourcePlatform()).getDefaultSequenceTableName());
        }
        super.onConnect();
    }

    protected ValueReadQuery buildSelectQuery() {
        ValueReadQuery query = new ValueReadQuery();
        query.addArgument(getNameFieldName());
        StringWriter writer = new StringWriter();
        writer.write("SELECT " + getCounterFieldName());
        writer.write(" FROM " + getQualifiedTableName());
        writer.write(" WHERE " + getNameFieldName());
        writer.write(" = #" + getNameFieldName());
        query.setSQLString(writer.toString());

        return query;
    }

    protected DataModifyQuery buildUpdateQuery() {
        DataModifyQuery query = new DataModifyQuery();
        query.addArgument(getNameFieldName());
        query.addArgument("PREALLOC_SIZE");
        StringWriter writer = new StringWriter();
        writer.write("UPDATE " + getQualifiedTableName());
        writer.write(" SET " + getCounterFieldName());
        writer.write(" = " + getCounterFieldName());
        writer.write(" + #PREALLOC_SIZE");
        writer.write(" WHERE " + getNameFieldName() + " = #" + getNameFieldName());
        query.setSQLString(writer.toString());

        return query;
    }

    protected ValueReadQuery buildTestQuery() {
        ValueReadQuery query = new ValueReadQuery();
        int pos = getName().indexOf("Seq");
        String tableName = name.substring(0, pos);
        StringWriter writer = new StringWriter();
        writer.write("SELECT MAX(id) as maxId");
        writer.write(" FROM " + tableName);
        query.setSQLString(writer.toString());

        return query;
    }

    @Override
    public void customize(Session sn) throws Exception {
        sn.getLogin().addSequence(new TableSequence("PaisSeq", 1));
        sn.getLogin().addSequence(new TableSequence("CalleSeq", 1));
        sn.getLogin().addSequence(new TableSequence("DireccionSeq", 1));
        sn.getLogin().addSequence(new TableSequence("LocalidadSeq", 1));
        sn.getLogin().addSequence(new TableSequence("PersonaSeq", 1));
        sn.getLogin().addSequence(new TableSequence("ProvinciaSeq", 1));
        sn.getLogin().addSequence(new TableSequence("SecurityObjectSeq", 1));
        sn.getLogin().addSequence(new TableSequence("SystemGroupRolesSeq", 1));
        sn.getLogin().addSequence(new TableSequence("SystemOptionSeq", 1));
        sn.getLogin().addSequence(new TableSequence("SystemOptionSecuritySeq", 1));
        sn.getLogin().addSequence(new TableSequence("SystemOptionSecurityOptionSeq", 1));
        sn.getLogin().addSequence(new TableSequence("SystemOptionSecurityValueSeq", 1));
        sn.getLogin().addSequence(new TableSequence("SystemRoleUsersSeq", 1));
        sn.getLogin().addSequence(new TableSequence("SystemUserPasswordSeq", 1));
        sn.getLogin().addSequence(new TableSequence("ContactoPersonaSeq", 1));
        sn.getLogin().addSequence(new TableSequence("EmpleadoSeq", 1));
        sn.getLogin().addSequence(new TableSequence("ClienteSeq", 1));
        sn.getLogin().addSequence(new TableSequence("ProveedorSeq", 1));
        sn.getLogin().addSequence(new TableSequence("SocioSeq", 1));
    }

}
