//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.smartlink.web.liquibase.database.core;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dm.jdbc.driver.DmdbConnection;
import liquibase.CatalogAndSchema;
import liquibase.GlobalConfiguration;
import liquibase.Scope;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.exception.ValidationErrors;
import liquibase.executor.ExecutorService;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SequenceCurrentValueFunction;
import liquibase.statement.SequenceNextValueFunction;
import liquibase.statement.UniqueConstraint;
import liquibase.statement.core.RawCallStatement;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Catalog;
import liquibase.structure.core.Index;
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Schema;
import liquibase.util.JdbcUtil;
import liquibase.util.StringUtil;

public class OracleDatabase extends AbstractJdbcDatabase {
    private static final String PROXY_USER_REGEX = ".*(?:thin|oci)\\:(.+)/@.*";
    public static final Pattern PROXY_USER_PATTERN = Pattern.compile(".*(?:thin|oci)\\:(.+)/@.*");
    private static final String VERSION_REGEX = "(\\d+)\\.(\\d+)\\..*";
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\..*");
    public static final String PRODUCT_NAME = "oracle";
    private static final ResourceBundle coreBundle = ResourceBundle.getBundle("liquibase/i18n/liquibase-core");
    protected final int SHORT_IDENTIFIERS_LENGTH = 30;
    protected final int LONG_IDENTIFIERS_LEGNTH = 128;
    public static final int ORACLE_12C_MAJOR_VERSION = 12;
    public static final int ORACLE_23C_MAJOR_VERSION = 23;
    private final Set<String> reservedWords = new HashSet();
    private Set<String> userDefinedTypes;
    private Map<String, String> savedSessionNlsSettings;
    private Boolean canAccessDbaRecycleBin;
    private Integer databaseMajorVersion;
    private Integer databaseMinorVersion;

    public OracleDatabase() {
        super.unquotedObjectsAreUppercased = true;
        super.setCurrentDateTimeFunction("SYSTIMESTAMP");
        this.dateFunctions.add(new DatabaseFunction("SYSDATE"));
        this.dateFunctions.add(new DatabaseFunction("SYSTIMESTAMP"));
        this.dateFunctions.add(new DatabaseFunction("CURRENT_TIMESTAMP"));
        super.sequenceNextValueFunction = "%s.nextval";
        super.sequenceCurrentValueFunction = "%s.currval";
    }

    public int getPriority() {
        return 1;
    }

    private void tryProxySession(String url, Connection con) {
        Matcher m = PROXY_USER_PATTERN.matcher(url);
        if (m.matches()) {
            Properties props = new Properties();
            props.put("PROXY_USER_NAME", m.group(1));

            Exception e;
            Method method;
            try {
                method = con.getClass().getMethod("openProxySession", Integer.TYPE, Properties.class);
                method.setAccessible(true);
                method.invoke(con, 1, props);
            } catch (Exception var8) {
                e = var8;
                Scope.getCurrentScope().getLog(this.getClass()).info("Could not open proxy session on OracleDatabase: " + e.getCause().getMessage());
                return;
            }

            try {
                method = con.getClass().getMethod("isProxySession");
                method.setAccessible(true);
                boolean b = (Boolean)method.invoke(con);
                if (!b) {
                    Scope.getCurrentScope().getLog(this.getClass()).info("Proxy session not established on OracleDatabase: ");
                }
            } catch (Exception var7) {
                e = var7;
                Scope.getCurrentScope().getLog(this.getClass()).info("Could not open proxy session on OracleDatabase: " + e.getCause().getMessage());
            }
        }

    }

    public void setConnection(DatabaseConnection conn) {
        this.reservedWords.addAll(Arrays.asList("GROUP", "USER", "SESSION", "PASSWORD", "RESOURCE", "START", "SIZE", "UID", "DESC", "ORDER"));
        Connection sqlConn = null;
        if (!(conn instanceof OfflineConnection)) {
            Exception e;
            try {
                if (conn instanceof JdbcConnection) {
                    sqlConn = ((JdbcConnection)conn).getWrappedConnection();
                }
            } catch (Exception var29) {
                e = var29;
                throw new UnexpectedLiquibaseException(e);
            }

            if (sqlConn != null) {
                this.tryProxySession(conn.getURL(), sqlConn);

                try {
                    this.reservedWords.addAll(Arrays.asList(sqlConn.getMetaData().getSQLKeywords().toUpperCase().split(",\\s*")));
                } catch (SQLException var28) {
                    SQLException e1 = var28;
                    Scope.getCurrentScope().getLog(this.getClass()).info("Could get sql keywords on OracleDatabase: " + e1.getMessage());
                }

                try {
                    Method method = sqlConn.getClass().getMethod("setRemarksReporting", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(sqlConn, true);
                } catch (Exception var27) {
                    e = var27;
                    Scope.getCurrentScope().getLog(this.getClass()).info("Could not set remarks reporting on OracleDatabase: " + e.getMessage());
                }

                CallableStatement statement = null;

                String sql;
                try {
                    DatabaseMetaData metaData = sqlConn.getMetaData();
                    Connection connection = metaData.getConnection();
                    if (connection instanceof DmdbConnection) {
                        String compatibleVersion = "11.2.0.4.0";
                        Matcher majorVersionMatcher = Pattern.compile("(\\d+)\\.(\\d+)\\..").matcher(compatibleVersion);
                        if (majorVersionMatcher.matches()) {
                            this.databaseMajorVersion = Integer.valueOf(majorVersionMatcher.group(1));
                            this.databaseMinorVersion = Integer.valueOf(majorVersionMatcher.group(2));
                        }
                    }else {
                        statement = sqlConn.prepareCall("{call DBMS_UTILITY.DB_VERSION(?,?)}");
                        statement.registerOutParameter(1, 12);
                        statement.registerOutParameter(2, 12);
                        statement.execute();
                        String compatibleVersion = statement.getString(2);
                        if (compatibleVersion != null) {
                            Matcher majorVersionMatcher = VERSION_PATTERN.matcher(compatibleVersion);
                            if (majorVersionMatcher.matches()) {
                                this.databaseMajorVersion = Integer.valueOf(majorVersionMatcher.group(1));
                                this.databaseMinorVersion = Integer.valueOf(majorVersionMatcher.group(2));
                            }
                        }
                    }

                } catch (SQLException var25) {
                    SQLException e2 = var25;
                    sql = "Cannot read from DBMS_UTILITY.DB_VERSION: " + e2.getMessage();
                    Scope.getCurrentScope().getLog(this.getClass()).info("Could not set check compatibility mode on OracleDatabase, assuming not running in any sort of compatibility mode: " + sql);
                } finally {
                    JdbcUtil.closeStatement(statement);
                }

                if (GlobalConfiguration.DDL_LOCK_TIMEOUT.getCurrentValue() != null) {
                    int timeoutValue = (Integer)GlobalConfiguration.DDL_LOCK_TIMEOUT.getCurrentValue();
                    Scope.getCurrentScope().getLog(this.getClass()).fine("Setting DDL_LOCK_TIMEOUT value to " + timeoutValue);
                    sql = "ALTER SESSION SET DDL_LOCK_TIMEOUT=" + timeoutValue;
                    PreparedStatement ddlLockTimeoutStatement = null;

                    try {
                        ddlLockTimeoutStatement = sqlConn.prepareStatement(sql);
                        ddlLockTimeoutStatement.execute();
                    } catch (SQLException var23) {
                        SQLException sqle = var23;
                        Scope.getCurrentScope().getUI().sendErrorMessage("Unable to set the DDL_LOCK_TIMEOUT_VALUE: " + sqle.getMessage(), sqle);
                        Scope.getCurrentScope().getLog(this.getClass()).warning("Unable to set the DDL_LOCK_TIMEOUT_VALUE: " + sqle.getMessage(), sqle);
                    } finally {
                        JdbcUtil.closeStatement(ddlLockTimeoutStatement);
                    }
                }
            }
        }

        super.setConnection(conn);
    }

    public String getShortName() {
        return "oracle";
    }

    protected String getDefaultDatabaseProductName() {
        return "Oracle";
    }

    public int getDatabaseMajorVersion() throws DatabaseException {
        return this.databaseMajorVersion == null ? super.getDatabaseMajorVersion() : this.databaseMajorVersion;
    }

    public int getDatabaseMinorVersion() throws DatabaseException {
        return this.databaseMinorVersion == null ? super.getDatabaseMinorVersion() : this.databaseMinorVersion;
    }

    public Integer getDefaultPort() {
        return 1521;
    }

    public String getJdbcCatalogName(CatalogAndSchema schema) {
        return null;
    }

    public String getJdbcSchemaName(CatalogAndSchema schema) {
        return this.correctObjectName(schema.getCatalogName() == null ? schema.getSchemaName() : schema.getCatalogName(), Schema.class);
    }

    protected String getAutoIncrementClause(String generationType, Boolean defaultOnNull) {
        if (StringUtil.isEmpty(generationType)) {
            return super.getAutoIncrementClause();
        } else {
            String autoIncrementClause = "GENERATED %s AS IDENTITY";
            String generationStrategy = generationType;
            if (Boolean.TRUE.equals(defaultOnNull) && generationType.toUpperCase().equals("BY DEFAULT")) {
                generationStrategy = generationStrategy + " ON NULL";
            }

            return String.format(autoIncrementClause, generationStrategy);
        }
    }

    public String generatePrimaryKeyName(String tableName) {
        return tableName.length() > 27 ? "PK_" + tableName.toUpperCase(Locale.US).substring(0, 27) : "PK_" + tableName.toUpperCase(Locale.US);
    }

    public boolean supportsInitiallyDeferrableColumns() {
        return true;
    }

    public boolean isReservedWord(String objectName) {
        return this.reservedWords.contains(objectName.toUpperCase());
    }

    public boolean supportsSequences() {
        return true;
    }

    public boolean supportsSchemas() {
        return false;
    }

    protected String getConnectionCatalogName() throws DatabaseException {
        if (this.getConnection() instanceof OfflineConnection) {
            return this.getConnection().getCatalog();
        } else if (!(this.getConnection() instanceof JdbcConnection)) {
            return this.defaultCatalogName;
        } else {
            try {
                return (String)((ExecutorService)Scope.getCurrentScope().getSingleton(ExecutorService.class)).getExecutor("jdbc", this).queryForObject(new RawCallStatement("select sys_context( 'userenv', 'current_schema' ) from dual"), String.class);
            } catch (Exception var2) {
                Exception e = var2;
                Scope.getCurrentScope().getLog(this.getClass()).info("Error getting default schema", e);
                return null;
            }
        }
    }

    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return "oracle".equalsIgnoreCase(conn.getDatabaseProductName());
    }

    public String getDefaultDriver(String url) {
        return url.startsWith("jdbc:oracle") ? "oracle.jdbc.OracleDriver" : null;
    }

    public String getDefaultCatalogName() {
        String defaultCatalogName = super.getDefaultCatalogName();
        if (Boolean.TRUE.equals(GlobalConfiguration.PRESERVE_SCHEMA_CASE.getCurrentValue())) {
            return defaultCatalogName;
        } else {
            return defaultCatalogName == null ? null : defaultCatalogName.toUpperCase(Locale.US);
        }
    }

    public String getDateLiteral(String isoDate) {
        String normalLiteral = super.getDateLiteral(isoDate);
        if (this.isDateOnly(isoDate)) {
            return "TO_DATE(" + normalLiteral + ", 'YYYY-MM-DD')";
        } else if (this.isTimeOnly(isoDate)) {
            return "TO_DATE(" + normalLiteral + ", 'HH24:MI:SS')";
        } else if (this.isTimestamp(isoDate)) {
            return "TO_TIMESTAMP(" + normalLiteral + ", 'YYYY-MM-DD HH24:MI:SS.FF')";
        } else if (this.isDateTime(isoDate)) {
            int seppos = normalLiteral.lastIndexOf(46);
            if (seppos != -1) {
                normalLiteral = normalLiteral.substring(0, seppos) + "'";
            }

            return "TO_DATE(" + normalLiteral + ", 'YYYY-MM-DD HH24:MI:SS')";
        } else {
            return "UNSUPPORTED:" + isoDate;
        }
    }

    public boolean isSystemObject(DatabaseObject example) {
        if (example == null) {
            return false;
        } else if (this.isLiquibaseObject(example)) {
            return false;
        } else {
            if (example instanceof Schema) {
                label131: {
                    if (!"SYSTEM".equals(example.getName()) && !"SYS".equals(example.getName()) && !"CTXSYS".equals(example.getName()) && !"XDB".equals(example.getName())) {
                        if (!"SYSTEM".equals(example.getSchema().getCatalogName()) && !"SYS".equals(example.getSchema().getCatalogName()) && !"CTXSYS".equals(example.getSchema().getCatalogName()) && !"XDB".equals(example.getSchema().getCatalogName())) {
                            break label131;
                        }

                        return true;
                    }

                    return true;
                }
            } else if (this.isSystemObject(example.getSchema())) {
                return true;
            }

            if (example instanceof Catalog) {
                if ("SYSTEM".equals(example.getName()) || "SYS".equals(example.getName()) || "CTXSYS".equals(example.getName()) || "XDB".equals(example.getName())) {
                    return true;
                }
            } else if (example.getName() != null) {
                if (example.getName().startsWith("BIN$")) {
                    boolean filteredInOriginalQuery = this.canAccessDbaRecycleBin();
                    if (!filteredInOriginalQuery) {
                        filteredInOriginalQuery = StringUtil.trimToEmpty(example.getSchema().getName()).equalsIgnoreCase(this.getConnection().getConnectionUserName());
                    }

                    if (!filteredInOriginalQuery) {
                        return true;
                    }

                    return !(example instanceof PrimaryKey) && !(example instanceof Index) && !(example instanceof UniqueConstraint);
                }

                if (example.getName().startsWith("AQ$")) {
                    return true;
                }

                if (example.getName().startsWith("DR$")) {
                    return true;
                }

                if (example.getName().startsWith("SYS_IOT_OVER")) {
                    return true;
                }

                if ((example.getName().startsWith("MDRT_") || example.getName().startsWith("MDRS_")) && example.getName().endsWith("$")) {
                    return true;
                }

                if (example.getName().startsWith("MLOG$_")) {
                    return true;
                }

                if (example.getName().startsWith("RUPD$_")) {
                    return true;
                }

                if (example.getName().startsWith("WM$_")) {
                    return true;
                }

                if ("CREATE$JAVA$LOB$TABLE".equals(example.getName())) {
                    return true;
                }

                if ("JAVA$CLASS$MD5$TABLE".equals(example.getName())) {
                    return true;
                }

                if (example.getName().startsWith("ISEQ$$_")) {
                    return true;
                }

                if (example.getName().startsWith("USLOG$")) {
                    return true;
                }

                if (example.getName().startsWith("SYS_FBA")) {
                    return true;
                }
            }

            return super.isSystemObject(example);
        }
    }

    public boolean supportsTablespaces() {
        return true;
    }

    public boolean supportsAutoIncrement() {
        boolean isAutoIncrementSupported = false;

        try {
            if (this.getDatabaseMajorVersion() >= 12) {
                isAutoIncrementSupported = true;
            }
        } catch (DatabaseException var3) {
            isAutoIncrementSupported = false;
        }

        return isAutoIncrementSupported;
    }

    public boolean supportsRestrictForeignKeys() {
        return false;
    }

    public int getDataTypeMaxParameters(String dataTypeName) {
        if ("BINARY_FLOAT".equals(dataTypeName.toUpperCase())) {
            return 0;
        } else {
            return "BINARY_DOUBLE".equals(dataTypeName.toUpperCase()) ? 0 : super.getDataTypeMaxParameters(dataTypeName);
        }
    }

    public String getSystemTableWhereClause(String tableNameColumn) {
        List<String> clauses = new ArrayList(Arrays.asList("BIN$", "AQ$", "DR$", "SYS_IOT_OVER", "MLOG$_", "RUPD$_", "WM$_", "ISEQ$$_", "USLOG$", "SYS_FBA"));
        clauses.replaceAll((s) -> {
            return tableNameColumn + " NOT LIKE '" + s + "%'";
        });
        return "(" + StringUtil.join(clauses, " AND ") + ")";
    }

    public boolean jdbcCallsCatalogsSchemas() {
        return true;
    }

    public Set<String> getUserDefinedTypes() {
        if (this.userDefinedTypes == null) {
            this.userDefinedTypes = new HashSet();
            if (this.getConnection() != null && !(this.getConnection() instanceof OfflineConnection)) {
                try {
                    try {
                        this.userDefinedTypes.addAll(((ExecutorService)Scope.getCurrentScope().getSingleton(ExecutorService.class)).getExecutor("jdbc", this).queryForList(new RawSqlStatement("SELECT DISTINCT TYPE_NAME FROM ALL_TYPES"), String.class));
                    } catch (DatabaseException var2) {
                        this.userDefinedTypes.addAll(((ExecutorService)Scope.getCurrentScope().getSingleton(ExecutorService.class)).getExecutor("jdbc", this).queryForList(new RawSqlStatement("SELECT TYPE_NAME FROM USER_TYPES"), String.class));
                    }
                } catch (DatabaseException var3) {
                }
            }
        }

        return this.userDefinedTypes;
    }

    public String generateDatabaseFunctionValue(DatabaseFunction databaseFunction) {
        if (databaseFunction != null && "current_timestamp".equalsIgnoreCase(databaseFunction.toString())) {
            return databaseFunction.toString();
        } else if (!(databaseFunction instanceof SequenceNextValueFunction) && !(databaseFunction instanceof SequenceCurrentValueFunction)) {
            return super.generateDatabaseFunctionValue(databaseFunction);
        } else {
            String quotedSeq = super.generateDatabaseFunctionValue(databaseFunction);
            return quotedSeq.replaceFirst("\"([^.\"]+)\\.([^.\"]+)\"", "\"$1\".\"$2\"");
        }
    }

    public ValidationErrors validate() {
        ValidationErrors errors = super.validate();
        DatabaseConnection connection = this.getConnection();
        if (connection != null && !(connection instanceof OfflineConnection)) {
            if (!this.canAccessDbaRecycleBin()) {
                errors.addWarning(this.getDbaRecycleBinWarning());
            }

            return errors;
        } else {
            Scope.getCurrentScope().getLog(this.getClass()).info("Cannot validate offline database");
            return errors;
        }
    }

    public String getDbaRecycleBinWarning() {
        return "Liquibase needs to access the DBA_RECYCLEBIN table so we can automatically handle the case where constraints are deleted and restored. Since Oracle doesn't properly restore the original table names referenced in the constraint, we use the information from the DBA_RECYCLEBIN to automatically correct this issue.\n\nThe user you used to connect to the database (" + this.getConnection().getConnectionUserName() + ") needs to have \"SELECT ON SYS.DBA_RECYCLEBIN\" permissions set before we can perform this operation. Please run the following SQL to set the appropriate permissions, and try running the command again.\n\n     GRANT SELECT ON SYS.DBA_RECYCLEBIN TO " + this.getConnection().getConnectionUserName() + ";";
    }

    public boolean canAccessDbaRecycleBin() {
        if (this.canAccessDbaRecycleBin == null) {
            DatabaseConnection connection = this.getConnection();
            if (connection == null || connection instanceof OfflineConnection) {
                return false;
            }

            Statement statement = null;

            try {
                statement = ((JdbcConnection)connection).createStatement();
                ResultSet resultSet = statement.executeQuery("select 1 from dba_recyclebin where 0=1");
                resultSet.close();
                this.canAccessDbaRecycleBin = true;
            } catch (Exception var7) {
                Exception e = var7;
                if (e instanceof SQLException && e.getMessage().startsWith("ORA-00942")) {
                    this.canAccessDbaRecycleBin = false;
                } else {
                    Scope.getCurrentScope().getLog(this.getClass()).warning("Cannot check dba_recyclebin access", e);
                    this.canAccessDbaRecycleBin = false;
                }
            } finally {
                JdbcUtil.close((ResultSet)null, statement);
            }
        }

        return this.canAccessDbaRecycleBin;
    }

    public boolean supportsNotNullConstraintNames() {
        return true;
    }

    public boolean isValidOracleIdentifier(String identifier, Class<? extends DatabaseObject> type) {
        if (identifier != null && identifier.length() >= 1) {
            if (!identifier.matches("^(i?)[A-Z][A-Z0-9\\$\\_\\#]*$")) {
                return false;
            } else {
                return identifier.length() <= 128;
            }
        } else {
            return false;
        }
    }

    public int getIdentifierMaximumLength() {
        try {
            if (this.getDatabaseMajorVersion() < 12) {
                return 30;
            } else {
                return this.getDatabaseMajorVersion() == 12 && this.getDatabaseMinorVersion() <= 1 ? 30 : 128;
            }
        } catch (DatabaseException var2) {
            DatabaseException ex = var2;
            throw new UnexpectedLiquibaseException("Cannot determine the Oracle database version number", ex);
        }
    }
}
